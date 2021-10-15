package org.imgoing.api.service;

import lombok.RequiredArgsConstructor;
import org.imgoing.api.domain.entity.Plan;
import org.imgoing.api.domain.entity.RouteSearcher;
import org.imgoing.api.dto.route.RouteSearchRequestDto;
import org.imgoing.api.repository.PlanRepository;
import org.imgoing.api.repository.PlantaskRepository;
import org.imgoing.api.support.ImgoingError;
import org.imgoing.api.support.ImgoingException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PlanService {
    private final PlanRepository planRepository;
    private final RouteSearcher routeSearcher;

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
    public Plan create(Plan newPlan) {
        return planRepository.save(newPlan);
    }

    @Transactional
    public void delete(Plan plan) {
        planRepository.delete(plan);
    }

    @Transactional
    public LocalDateTime getTimeRemainingUntilRecentPlan () {
        LocalDateTime now = LocalDateTime.now();
        Plan recentPlan = planRepository.getByArrivalAtLessThanOrderByArrivalAtAsc(now)
                .orElseThrow(() -> new ImgoingException(ImgoingError.BAD_REQUEST, "시간을 계산할 수 있는 Plan이 없습니다."));
        // TODO: Hardcoding된 Lat, Lng 인풋으로 받기
        double startLng = 126.9027279;
        double startLat = 37.5349277;
        RouteSearchRequestDto requestDto = new RouteSearchRequestDto(
                startLng,
                startLat,
                recentPlan.getArrivalLng(),
                recentPlan.getArrivalLat(),
                0
        );
        int routeAverageMins = (int) Math.ceil(routeSearcher.calcRouteAverageTime(requestDto));
        LocalDateTime recentPlanArrivalAt = recentPlan.getArrivalAt();
        // TODO: PlanTask들의 준비시간 합해서 계산해서 minus 해주기
        return recentPlanArrivalAt.minusMinutes(routeAverageMins);
    }
}
