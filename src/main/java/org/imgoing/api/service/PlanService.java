package org.imgoing.api.service;

import lombok.RequiredArgsConstructor;
import org.imgoing.api.domain.entity.Plan;
import org.imgoing.api.domain.entity.Task;
import org.imgoing.api.dto.PlanDto;
import org.imgoing.api.repository.PlanRepository;
import org.imgoing.api.support.ImgoingError;
import org.imgoing.api.support.ImgoingException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PlanService {
    private final PlanRepository planRepository;
    private final TaskService taskService;

    @Transactional
    public Plan createPlan(Plan plan) {
        return planRepository.save(plan);
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
        Plan plan = getPlanById(planDto.getId());
        validateAuthorizedUser(userId, plan);

        // plan update
        planDto.update(plan);

        // update된 plan save
        plan = planRepository.save(plan);

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
        for(Task task : plan.getTaskList()) {
            if(!task.getIsBookmarked()) {
                taskService.delete(task);
            }
        }
    }

    // plan의 작성자와 현재 로그인한 사용자가 일치하는지 확인하는 함수
    public void validateAuthorizedUser(Long accessUserId, Plan plan) {
        long userId = plan.getUser().getId();
        if(accessUserId != userId) {
            throw new ImgoingException(ImgoingError.NOT_PERMITTED);
        }
    }
}