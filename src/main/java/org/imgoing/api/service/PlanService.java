package org.imgoing.api.service;

import lombok.RequiredArgsConstructor;
import org.imgoing.api.domain.entity.*;
import org.imgoing.api.dto.PlanDto;
import org.imgoing.api.dto.TaskDto;
import org.imgoing.api.mapper.PlanMapper;
import org.imgoing.api.mapper.TaskMapper;
import org.imgoing.api.domain.entity.User;
import org.imgoing.api.domain.vo.RemainingTimeInfoVo;
import org.imgoing.api.dto.route.RouteSearchRequest;
import org.imgoing.api.repository.PlanRepository;
import org.imgoing.api.repository.PlantaskRepository;
import org.imgoing.api.repository.TaskRepository;
import org.imgoing.api.support.ImgoingError;
import org.imgoing.api.support.ImgoingException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.time.Duration;
import java.time.LocalDateTime;
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
    private final PlanMapper planMapper;
    private final TaskMapper taskMapper;
    private final RouteSearcher routeSearcher;

    @Transactional
    public Plan createPlan(User user, PlanDto.Create planSaveRequest) {
        Plan savedPlan = planRepository.save(planMapper.toEntityForSave(user, planSaveRequest));

        List<TaskDto> taskDtos = planSaveRequest.getTask();
        List<Long> bookmarkedTaskIds = planSaveRequest.getBookmarkedTaskIds();
        if (taskDtos.isEmpty() && bookmarkedTaskIds.isEmpty()) {
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
    public List<Plan> getPlansByUser(User user) {
        return planRepository.findAllByUserId(user.getId());
    }

    @Transactional(readOnly = true)
    public Plan getPlan(Long userId, Long planId) {
        Plan plan = getPlanById(planId);
        validateAuthorizedUser(userId, plan);

        return plan;
    }

    @Transactional
    public Plan updatePlan(Long userId, PlanDto planDto) {
        Plan plan = getPlanById(planDto.getId()); // oldPlan
        validateAuthorizedUser(userId, plan);

        Plan newPlan = planMapper.toEntity(planDto);
        plan.updatePlan(newPlan);

        List<TaskDto> taskDtos = planDto.getTask();

        // TODO: 로직 수정

        // 기존 plantask  삭제
        plantaskService.deleteByPlanId(plan.getId());

        // 기존 task 삭제
        deleteTask(plan);

        List<Task> tasks = new ArrayList<>();

        // 준비항목이 없는 경우
        if(taskDtos.size() == 0) {
            plan.registerPlantask(tasks);
            return plan;
        }

        // 북마크가 아닌 준비항목은 task db에 저장
        tasks = taskService.saveAll(findNotBookmarkedTask(taskDtos));

        // plantask 등록
        tasks.addAll(findBookmarkedTask(taskDtos));
        plan.registerPlantask(tasks);

        // plantask db 저장
        plantaskService.saveAll(plan.getPlantasks());

        return plan;
    }

    @Transactional
    public void deletePlan(Long userId, Long planId) {
        Plan plan = getPlanById(planId);
        validateAuthorizedUser(userId, plan);
        
        // task 삭제
        deleteTask(plan);

        planRepository.delete(plan);
    }

    @Transactional(readOnly = true)
    public Plan getPlanById(Long planId) {
        return planRepository.findById(planId)
                .orElseThrow(() -> new ImgoingException(ImgoingError.BAD_REQUEST, "존재하지 않는 일정입니다."));
    }

    // 북마크가 아닌 task 삭제
    @Transactional
    public void deleteTask(Plan plan) {
        List<Task> tasks = plan.getTaskList().stream()
                .filter(task -> !task.getIsBookmarked())
                .collect(Collectors.toList());
        taskService.deleteAll(tasks);
    }

    // plan의 작성자와 현재 로그인한 사용자가 일치하는지 확인하는 함수
    public void validateAuthorizedUser(Long accessUserId, Plan plan) {
        long userId = plan.getUser().getId();
        if(accessUserId != userId) {
            throw new ImgoingException(ImgoingError.NOT_PERMITTED);
        }
    }

    public List<Task> findNotBookmarkedTask (List<TaskDto> taskDtos) {
        return taskDtos.stream()
                .filter(taskDto -> !taskDto.getIsBookmarked())
                .map(taskMapper::toEntity)
                .collect(Collectors.toList());
    }

    // 북마크 등록 안된 task 찾는 함수
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
                .orElseThrow(() -> new ImgoingException(ImgoingError.BAD_REQUEST, "시간을 계산할 수 있는 Plan이 없습니다."));
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
}
