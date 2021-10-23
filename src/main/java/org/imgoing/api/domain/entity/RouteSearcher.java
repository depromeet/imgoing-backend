package org.imgoing.api.domain.entity;

import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.imgoing.api.dto.route.OdsayRouteSearchResponse;
import org.imgoing.api.dto.route.RouteSearchRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;

@Slf4j
@NoArgsConstructor
@Component
public class RouteSearcher {
    private static final RestTemplate routeSearchClient = new RestTemplate();
    private static final String url = "https://api.odsay.com/v1/api/searchPubTransPathT";

    @Value("${keys.odsayApiKey}")
    private String apiKey;

    private OdsayRouteSearchResponse searchAllRoutes(RouteSearchRequest requestDto) {
        UriComponentsBuilder uriComponentsBuilder = UriComponentsBuilder.fromHttpUrl(url)
                .queryParam("apiKey", apiKey)
                .queryParam("SX", requestDto.getStartLng())
                .queryParam("SY", requestDto.getStartLat())
                .queryParam("EX", requestDto.getEndLng())
                .queryParam("EY", requestDto.getEndLat())
                .queryParam("OPT", requestDto.getSortCriterion());
        ResponseEntity<OdsayRouteSearchResponse> response = routeSearchClient.getForEntity(
                URI.create(uriComponentsBuilder.build().toUriString()),
                OdsayRouteSearchResponse.class);
        log.info("statusCode: {}", response.getStatusCode());
        return response.getBody();
    }

    public double calcRouteAverageTime(RouteSearchRequest requestDto) {
        OdsayRouteSearchResponse routeSearchResponseDto = this.searchAllRoutes(requestDto);
        return routeSearchResponseDto.getResult().getPath().stream()
                .mapToDouble(path -> path.getInfo().getTotalTime())
                .limit(5)
                .average()
                .orElse(0.0);
    }
}
