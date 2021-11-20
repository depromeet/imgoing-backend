package org.imgoing.api.dto.route;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.imgoing.api.domain.vo.RemainingTimeInfoVo;

import java.time.Duration;
import java.time.LocalDateTime;

@Getter
public class RemainingTimeResponse {
    private final RemainingTime remainingTime;
    private final int routeAverageTimeAsMins;
    private final int totalReadyTimeAsMins;
    private final LocalDateTime recentPlanArrivalAt;

    @RequiredArgsConstructor
    @Getter
    public static class RemainingTime {
        private final long hours;
        private final int minutes;
    }

    public RemainingTimeResponse(RemainingTimeInfoVo remainingTimeInfoVo) {
        Duration remainingTime = remainingTimeInfoVo.getRemainingTime();
        long hours = remainingTime.toDaysPart() * 24 + remainingTime.toHoursPart();
        int minutes = remainingTime.toMinutesPart();
        this.remainingTime = new RemainingTime(hours, minutes);
        this.routeAverageTimeAsMins = remainingTimeInfoVo.getRouteAverageTimeAsMins();
        this.totalReadyTimeAsMins = remainingTimeInfoVo.getTotalReadyTimeAsMins();
        this.recentPlanArrivalAt = remainingTimeInfoVo.getRecentPlanArrivalAt();
    }
}