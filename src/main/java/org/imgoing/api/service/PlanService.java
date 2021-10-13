package org.imgoing.api.service;

import lombok.RequiredArgsConstructor;
import org.imgoing.api.domain.entity.Plan;
import org.imgoing.api.domain.entity.User;
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

    @Transactional(readOnly = true)
    public Plan getPlanById(Long id) {
        return planRepository.findById(id)
                .orElseThrow(() -> new ImgoingException(ImgoingError.BAD_REQUEST, "존재하지 않는 일정입니다."));
    }

    @Transactional(readOnly = true)
    public List<Plan> getPlanByUserId(Long userId) {
        // 일단 findAll
        return planRepository.findAll();
    }

    @Transactional
    public Plan create(User user, Plan plan) {
        plan.addUser(user);
        return planRepository.save(plan);
    }

    @Transactional
    public void delete(Plan plan) {
        planRepository.delete(plan);
    }
}
