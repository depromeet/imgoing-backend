package org.imgoing.api.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;

@SuperBuilder
@Getter
@Setter
@NoArgsConstructor
public class PlanDto {
    @ApiModelProperty(value = "일정 이름")
    private String name;

    @ApiModelProperty(value = "출발 장소 이름")
    private String departure_name;

    @ApiModelProperty(value = "출발 위도")
    private Double departure_lat;

    @ApiModelProperty(value = "출발 경도")
    private Double departure_lng;

    @ApiModelProperty(value = "도착 장소 이름")
    private String arrival_name;

    @ApiModelProperty(value = "도착 위도")
    private Double arrival_lat;

    @ApiModelProperty(value = "도착 경도")
    private Double arrival_lng;

    @ApiModelProperty(value = "도착 시간")
    private LocalDateTime arrival_at;
}
