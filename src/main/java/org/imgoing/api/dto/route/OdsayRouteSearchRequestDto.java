package org.imgoing.api.dto.route;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public class OdsayRouteSearchRequestDto {
    private final String apiKey;
    private final double SX;
    private final double SY;
    private final double EX;
    private final double EY;
    private final int OPT;
}
