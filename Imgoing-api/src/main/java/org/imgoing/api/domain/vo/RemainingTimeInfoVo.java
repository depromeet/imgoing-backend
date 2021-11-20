package org.imgoing.api.domain.vo;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.time.Duration;
import java.time.LocalDateTime;

@RequiredArgsConstructor
@Getter
public class RemainingTimeInfoVo {
    private final Duration remainingTime;
    private final int routeAverageTimeAsMins;
    private final int totalReadyTimeAsMins;
    private final LocalDateTime recentPlanArrivalAt;
}
