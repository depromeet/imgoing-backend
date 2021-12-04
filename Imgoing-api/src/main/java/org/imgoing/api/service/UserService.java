package org.imgoing.api.service;

import lombok.RequiredArgsConstructor;
import org.imgoing.api.domain.entity.Plan;
import org.imgoing.api.domain.entity.User;
import org.imgoing.api.dto.user.UserMonthlyStatResponse;
import org.imgoing.api.repository.PlanRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@RequiredArgsConstructor
@Service
public class UserService {
    private final PlanRepository planRepository;

    @Transactional(readOnly = true)
    public long[] getMonthlyUserStats (User user) {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime startOfMonth = now.minusDays(now.getDayOfMonth());
        List<Plan> planOneMonthAgo = planRepository.findAllByUserAndActualArrivalAtGreaterThanEqual(user, startOfMonth);
        long totalPlanCount = planOneMonthAgo.size();
        long latePlanCount = planOneMonthAgo.stream().filter(Plan::getIsUserLate).count();
        return new long[]{ totalPlanCount, latePlanCount };
    }
}
