package org.imgoing.api.service;

import lombok.RequiredArgsConstructor;
import org.imgoing.api.domain.entity.Plan;
import org.imgoing.api.domain.entity.Plantask;
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
    private final PlanMapper planMapper;
    private final TaskMapper taskMapper;

    @Transactional
    public PlanDto createPlan(User user, PlanDto planDto) {
        Plan plan = planMapper.toEntity(planDto);
        plan.addUser(user);

        plan = planRepository.save(plan);

        List<TaskDto> taskDtos = planDto.getTaskDtos();

        // 준비항목이 없을 때
        if(taskDtos.size() == 0) {
            return planMapper.toDto(plan, taskDtos);
        }

        // 새로운 task와 bookmarked된 task 분류
        List<Task> newTasks = new ArrayList<>();
        List<Task> bookmarkedTasks = new ArrayList<>();
        for(TaskDto taskDto : taskDtos) {
            if (!taskDto.getIsBookmarked()) {
                newTasks.add(taskMapper.toEntity(taskDto));
            } else {
                bookmarkedTasks.add(taskMapper.toEntity(taskDto));
            }
        }

        // 새로운 task일 경우: task table에 먼저 저장
        // newTasks = taskService.saveTasks(user, newTasks);

        // plantask db 저장 TODO
        newTasks.addAll(bookmarkedTasks);
        // plantaskService.savePlanTasks(plan.getid(), newTasks);

        return planMapper.toDto(plan, getTaskDtoList(plan));
    }

    @Transactional(readOnly = true)
    public List<PlanDto> getPlans(Long userId) {
        List<Plan> plans =  planRepository.findAll();

        return plans.stream()
                .map(plan -> planMapper.toDto(plan, getTaskDtoList(plan)))
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public PlanDto getPlan(Long userId, Long planId) {
        Plan plan = getPlanById(planId);
        validateAuthorizedUser(userId, plan);

        return planMapper.toDto(plan, getTaskDtoList(plan));
    }

    @Transactional
    public PlanDto updatePlan(Long userId, PlanDto planDto) {
        Plan plan = getPlanById(planDto.getId());
        validateAuthorizedUser(userId, plan);

        // plan update
        planDto.update(plan);

        // update된 plan save
        plan = planRepository.save(plan);

        // task update
        // taskService.update(planDto.getTasks());

        // plantask update TODO

        return planMapper.toDto(plan, getTaskDtoList(plan));
    }

    @Transactional
    public void deletePlan(Long userId, Long planId) {
        Plan plan = getPlanById(planId);
        validateAuthorizedUser(userId, plan);
        
        // task 삭제
        // taskService.delete(plan.getId());

        // plantask 삭제 TODO -> plan id 해당하는거 다 지우면 됨
        // plantaskService.delete(plan.getId());

        planRepository.delete(plan);
    }

    @Transactional(readOnly = true)
    public Plan getPlanById(Long id) {
        return planRepository.findById(id)
                .orElseThrow(() -> new ImgoingException(ImgoingError.BAD_REQUEST, "존재하지 않는 일정입니다."));
    }
    
    // plan에 속한 task를 모두 가져오는 함수
    @Transactional(readOnly = true)
    public List<TaskDto> getTaskDtoList(Plan plan) {
        return plan.getPlantasks().stream()
                .map(plantask -> taskMapper.toDto(plantask.getTask()))
                .collect(Collectors.toList());
    }

    // plan의 작성자와 현재 로그인한 사용자가 일치하는지 확인하는 함수
    private void validateAuthorizedUser(Long accessUserId, Plan plan) {
        long userId = plan.getUser().getId();
        if(accessUserId != userId) {
           throw new ImgoingException(ImgoingError.NOT_PERMITTED);
        }
    }
}