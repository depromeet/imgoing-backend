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
    public PlanDto create(User user, PlanDto planDto) {
        Plan plan = planMapper.toEntity(planDto);
        plan.addUser(user);

        long planId = planRepository.save(plan).getId();

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

        // 새로운 task일 경우 -> task table에 먼저 저장
        // newTasks = taskService.saveTasks(user, newTasks);

        // plantask db 저장
        newTasks.addAll(bookmarkedTasks);
        // plantaskService.savePlanTasks(planId, newTasks);

        taskDtos = newTasks.stream()
                .map(taskMapper::toDto)
                .collect(Collectors.toList());

        return planMapper.toDto(plan, taskDtos); // TODO: task id생성된 dto랑 plan dto랑 합쳐서 넘기기
    }

    @Transactional(readOnly = true)
    public Plan getPlanById(Long id) {
        return planRepository.findById(id)
                .orElseThrow(() -> new ImgoingException(ImgoingError.BAD_REQUEST, "존재하지 않는 일정입니다."));
    }

    @Transactional(readOnly = true)
    public List<Plan> getPlanByUserId(Long userId) {
        List<Plan> plans =  planRepository.findAll();

        // TODO task 붙여주기
        for (Plan plan : plans) {
            List<Plantask> plantasks = plan.getPlantasks();
            for (Plantask plantask : plantasks) {
                plantask.getTask();
            }
        }
        return planRepository.findAll();
    }

    @Transactional
    public Plan update(Long userId, PlanDto planDto) {
        Plan plan = getPlanById(planDto.getId());
        validateAuthorizedUser(userId, plan);

        // plan update
        planDto.update(plan);

        // task update
        // taskService.update(planDto.getTasks());

        // TODO task 붙여서 return
        return  planRepository.save(plan);
    }

    @Transactional
    public void delete(Long userId, Plan plan) {
        validateAuthorizedUser(userId, plan);
        
        // task 삭제
        // taskService.delete(plan.getId());

        // plantask 삭제
        // plantaskService.delete(plan.getId());

        planRepository.delete(plan);
    }

    // plan의 작성자와 현재 로그인한 사용자가 일치하는지 확인하는 함수
    private void validateAuthorizedUser(Long accessUserId, Plan plan) {
        long userId = plan.getUser().getId();
        if(accessUserId != userId) {
           throw new ImgoingException(ImgoingError.NOT_PERMITTED);
        }
    }
}