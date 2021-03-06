package org.imgoing.api.service;

import lombok.RequiredArgsConstructor;
import org.imgoing.api.domain.entity.*;
import org.imgoing.api.domain.vo.RemainingTimeInfoVo;
import org.imgoing.api.dto.plan.ImportantPlanDto;
import org.imgoing.api.dto.plan.PlanArrivalRequest;
import org.imgoing.api.dto.route.RouteSearchRequest;
import org.imgoing.api.dto.task.TaskDto;
import org.imgoing.api.mapper.TaskMapper;
import org.imgoing.api.repository.PlanRepository;
import org.imgoing.api.repository.PlantaskRepository;
import org.imgoing.api.repository.TaskRepository;
import org.imgoing.api.support.ImgoingError;
import org.imgoing.api.support.ImgoingException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PlanService {
    private final PlanRepository planRepository;
    private final TaskRepository taskRepository;
    private final PlantaskRepository plantaskRepository;
    private final TaskService taskService;
    private final PlantaskService plantaskService;
    private final TaskMapper taskMapper;
    private final RouteSearcher routeSearcher;

    @Transactional
    public Plan create(Plan newPlan, List<TaskDto> taskDtos, List<Long> bookmarkedTaskIds) {
        Plan savedPlan = planRepository.save(newPlan);

        if (taskDtos.isEmpty() && bookmarkedTaskIds.isEmpty()) {
            savedPlan.registerPlantasks(new ArrayList<>());
            return savedPlan;
        }

        List<Task> bookmarkedTasks = taskRepository.findAllByIdIn(bookmarkedTaskIds);
        List<Task> notBookmarkedTasks = taskRepository.saveAll(findNotBookmarkedTask(taskDtos));
        List<Task> tasks = new ArrayList<>();
        tasks.addAll(bookmarkedTasks);
        tasks.addAll(notBookmarkedTasks);

        List<Plantask> plantasks = plantaskRepository.saveAll(tasks.stream()
                .map(task -> Plantask.builder()
                        .plan(savedPlan)
                        .task(task)
                        .build()
                ).collect(Collectors.toList()));
        savedPlan.registerPlantasks(plantasks);
        return savedPlan;
    }

    @Transactional(readOnly = true)
    public List<Plan> getAll(User user) {
        LocalDateTime now = LocalDateTime.now();
        return planRepository.findByUserIdAndArrivalAtGreaterThanEqualOrderByArrivalAtAsc(user.getId(), now);
    }

    @Transactional
    public Plan modify(Plan oldPlan, Plan newPlan, List<TaskDto> taskDtos) {
        oldPlan.modify(newPlan);
        Plan modifiedPlan = planRepository.save(oldPlan);

        // TODO: ?????? ??????

        // ?????? plantask  ??????
        plantaskService.deleteByPlanId(modifiedPlan.getId());

        // ?????? task ??????
        deleteNotBookmarkedTask(modifiedPlan);

        List<Task> tasks = new ArrayList<>();

        // ??????????????? ?????? ??????
        if(taskDtos.size() == 0) {
            modifiedPlan.registerPlantask(tasks);
            return modifiedPlan;
        }

        // ???????????? ?????? ??????????????? task db??? ??????
        tasks = taskService.saveAll(findNotBookmarkedTask(taskDtos));

        // plantask ??????
        tasks.addAll(findBookmarkedTask(taskDtos));
        modifiedPlan.registerPlantask(tasks);

        // plantask db ??????
        plantaskService.saveAll(modifiedPlan.getPlantasks());

        return modifiedPlan;
    }

    @Transactional
    public void delete(Plan plan) {
        // task ??????
        deleteNotBookmarkedTask(plan);

        planRepository.delete(plan);
    }

    // ???????????? ?????? task ??????
    @Transactional
    public void deleteNotBookmarkedTask(Plan plan) {
        List<Task> tasks = plan.getTaskList().stream()
                .filter(task -> !task.getIsBookmarked())
                .collect(Collectors.toList());
        taskService.deleteAll(tasks);
    }

    @Transactional
    public ImportantPlanDto registerImportant(Long planId) {
        return new ImportantPlanDto(planId, planRepository.getById(planId).modifyImportantStatus());
    }

    @Transactional(readOnly = true)
    public List<Plan> getImportantList(Long userId) {
        return planRepository.findByUserIdAndIsImportantOrderByArrivalAtAsc(userId, true);
    }

    public List<Task> findNotBookmarkedTask (List<TaskDto> taskDtos) {
        return taskDtos.stream()
                .filter(taskDto -> !taskDto.getIsBookmarked())
                .map(taskMapper::toEntity)
                .collect(Collectors.toList());
    }

    public List<Task> findBookmarkedTask (List<TaskDto> taskDtos) {
        return taskDtos.stream()
                .filter(taskDto -> taskDto.getIsBookmarked())
                .map(taskMapper::toEntity)
                .collect(Collectors.toList());
    }

    @Transactional
    public RemainingTimeInfoVo getTimeRemainingUntilRecentPlan (User user) {
        LocalDateTime now = LocalDateTime.now();
        Plan recentPlan = planRepository.findTopByUserAndArrivalAtGreaterThanOrderByArrivalAtAsc(user, now)
                .orElseThrow(() -> new ImgoingException(ImgoingError.BAD_REQUEST, "????????? ????????? ??? ?????? Plan??? ????????????."));
        RouteSearchRequest requestDto = new RouteSearchRequest(
                recentPlan.getDepartureLng(),
                recentPlan.getDepartureLat(),
                recentPlan.getArrivalLng(),
                recentPlan.getArrivalLat(),
                0
        );
        int routeAverageMins = (int) Math.ceil(routeSearcher.calcRouteAverageTime(requestDto));
        int preparationMins = recentPlan.getPlantasks().stream().mapToInt(pt -> pt.getTask().getTime()).sum();
        LocalDateTime recentPlanArrivalAt = recentPlan.getArrivalAt();
        LocalDateTime preparationStartAt = recentPlanArrivalAt.minusMinutes(routeAverageMins + preparationMins);
        Duration remainingTime = Duration.between(now, preparationStartAt);
        return new RemainingTimeInfoVo(remainingTime, routeAverageMins, preparationMins, recentPlanArrivalAt);
    }

    @Transactional(readOnly = true)
    public List<Plan> getPlanHistoryDaysAgo (User user, Integer days) {
        LocalDateTime sevenDaysAgo = LocalDateTime.now().minusDays(days);
        return this.planRepository.findByUserAndArrivalAtGreaterThanEqualOrderByArrivalAtAsc(user, sevenDaysAgo);
    }

    @Transactional
    public void recordArrivalInformation (Long planId, PlanArrivalRequest planArrivalRequest) {
        Plan plan = this.planRepository.findById(planId).orElseThrow(() -> new ImgoingException(ImgoingError.BAD_REQUEST, "Plan??? ????????????."));
        LocalDateTime actualArrivalAt = planArrivalRequest.getActualArrivalAt();
        plan.recordArrivalOfAppointment(actualArrivalAt);
    }
}
