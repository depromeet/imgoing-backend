package org.imgoing.api.service;

import lombok.RequiredArgsConstructor;
import org.imgoing.api.domain.entity.Plan;
import org.imgoing.api.domain.entity.Task;
import org.imgoing.api.domain.entity.User;
import org.imgoing.api.dto.PlanDto;
import org.imgoing.api.dto.TaskDto;
import org.imgoing.api.mapper.PlanMapper;
import org.imgoing.api.mapper.TaskMapper;
import org.imgoing.api.repository.PlanRepository;
import org.imgoing.api.support.ImgoingError;
import org.imgoing.api.support.ImgoingException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PlanService {
    private final PlanRepository planRepository;
    private final TaskService taskService;
    private final PlantaskService plantaskService;
    private final PlanMapper planMapper;
    private final TaskMapper taskMapper;

    @Transactional
    public Plan createPlan(User user, PlanDto planDto) {
        Plan plan = planMapper.toEntity(planDto);
        plan.addUser(user);

        // plan 저장
        plan = planRepository.save(plan);
        
        List<TaskDto> taskDtos = planDto.getTask();
        List<Task> tasks = new ArrayList<>();

        // 준비항목이 없는 경우
        if(taskDtos.size() == 0) {
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

    @Transactional(readOnly = true)
    public List<Plan> getPlans(Long userId) {
        return planRepository.findAll();
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

    // 북마크 등록 된 task 찾는 함수
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
}