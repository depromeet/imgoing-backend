package org.imgoing.api.domain.entity;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.NoArgsConstructor;
import org.imgoing.api.dto.route.OdsayRouteSearchRequestDto;
import org.imgoing.api.dto.route.OdsayRouteSearchResponseDto;
import org.imgoing.api.dto.route.RouteSearchRequestDto;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.Map;

@NoArgsConstructor
@Component
public class RouteSearcher {
    private static final ObjectMapper mapper = new ObjectMapper();
    private static final RestTemplate routeSearchClient = new RestTemplate();
    private static final String url = "https://api.odsay.com/v1/api/searchPubTransPathT";

    @Value("${keys.odsayApiKey}")
    private String apiKey;

    private OdsayRouteSearchResponseDto searchAllRoutes(RouteSearchRequestDto requestDto) {
        UriComponentsBuilder uriComponentsBuilder = UriComponentsBuilder.fromHttpUrl(url);
        MultiValueMap params = new LinkedMultiValueMap<>();
        params.setAll(mapper.convertValue(
                new OdsayRouteSearchRequestDto(
                        apiKey,
                        requestDto.getStartLng(),
                        requestDto.getStartLat(),
                        requestDto.getEndLng(),
                        requestDto.getEndLat(),
                        requestDto.getSortCriterion()
                ), Map.class));
        uriComponentsBuilder.queryParams(params);
        OdsayRouteSearchResponseDto response = routeSearchClient.getForObject(
                uriComponentsBuilder.build().toUri(),
                OdsayRouteSearchResponseDto.class);
        return response;
    }

    public double calcRouteAverageTime(RouteSearchRequestDto requestDto) {
        OdsayRouteSearchResponseDto routeSearchResponseDto = this.searchAllRoutes(requestDto);
        return routeSearchResponseDto.getResult().getPath().stream()
                .mapToDouble(path -> path.getInfo().getTotalTime())
                .limit(5)
                .average()
                .orElse(0.0);
    }
}
