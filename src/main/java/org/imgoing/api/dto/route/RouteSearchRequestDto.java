package org.imgoing.api.dto.route;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public class RouteSearchRequestDto {
    private final double startLng;
    private final double startLat;
    private final double endLng;
    private final double endLat;
    private final int sortCriterion;
}
