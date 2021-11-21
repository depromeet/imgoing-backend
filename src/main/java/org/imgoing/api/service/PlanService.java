package org.imgoing.api.service;

import lombok.RequiredArgsConstructor;
import org.imgoing.api.domain.entity.*;
import org.imgoing.api.domain.vo.RemainingTimeInfoVo;
import org.imgoing.api.dto.route.RouteSearchRequest;
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
    private final PlantaskService plantaskService;
    private final RouteSearcher routeSearcher;

    @Transactional
    public Plan create(User user, Plan newPlan, List<Task> newTasks) {
        Plan savedPlan = planRepository.save(newPlan);

        if (newTasks.isEmpty()) {
            savedPlan.registerPlantasks(new ArrayList<>());
            return savedPlan;
        }

        List<Task> tasks = newTasks.stream()
                .map(task -> Task.builder()
                        .name(task.getName())
                        .time(task.getTime())
                        .user(user)
                        .build()
                )
                .collect(Collectors.toList());

        taskRepository.saveAll(tasks);

        List<Plantask> plantasks = new ArrayList<>();
        for(int i = 0; i < tasks.size(); i++) {
            plantasks.add(Plantask.builder()
                    .plan(savedPlan)
                    .task(tasks.get(i))
                    .sequence(i + 1)
                    .build()
            );
        }
        plantaskRepository.saveAll(plantasks);
        savedPlan.registerPlantasks(plantasks);

        return savedPlan;
    }

    @Transactional(readOnly = true)
    public List<Plan> getAll(User user) {
        LocalDateTime now = LocalDateTime.now();
        return planRepository.findByUserIdAndArrivalAtGreaterThanEqualOrderByArrivalAtAsc(user.getId(), now);
    }

    @Transactional
    public Plan modify(Plan oldPlan, Plan newPlan, List<Task> newTasks) {
        User user = oldPlan.getUser();
        List<Long> oldTaskIds = getTaskIdList(oldPlan);

        // 기존 plantask 삭제
        plantaskService.deleteByPlanId(oldPlan.getId());

        // 수정된 plan 저장
        oldPlan.modify(newPlan);
        Plan modifiedPlan = planRepository.save(oldPlan);

        List<Task> tasks = new ArrayList<>();

        // 준비항목이 없는 경우
        if(newTasks.size() == 0) {
            // 기존 task 삭제
            taskRepository.deleteByIdIn(oldTaskIds);

            modifiedPlan.modifyPlantasks(tasks);
            return modifiedPlan;
        }

        for(int i = 0; i < newTasks.size(); i++) {
            Task task = newTasks.get(i);
            // 새로운 task는 db에 저장
            if(!oldTaskIds.contains(newTasks.get(i).getId())) {
                task = taskRepository.save(
                        Task.builder()
                                .name(task.getName())
                                .time(task.getTime())
                                .user(user)
                                .build()
                );
            }
            tasks.add(task);
        }

        List<Long> newTaskIds = tasks.stream()
                .map(task -> task.getId())
                .collect(Collectors.toList());

        // 삭제할 task 목록
        List<Long> removeTaskIds = oldTaskIds.stream()
                .filter(oldTaskId -> !newTaskIds.contains(oldTaskId))
                .collect(Collectors.toList());
        taskRepository.deleteByIdIn(removeTaskIds);

        plantaskRepository.saveAll(modifiedPlan.modifyPlantasks(tasks));

        return modifiedPlan;
    }

    @Transactional
    public void delete(Plan plan) {
        taskRepository.deleteByIdIn(getTaskIdList(plan));
        planRepository.delete(plan);
    }

    // plan의 준비항목 id 반환하는 함수
    public List<Long> getTaskIdList(Plan plan) {
        return plan.getTaskList().stream()
                .map(task -> task.getId())
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
