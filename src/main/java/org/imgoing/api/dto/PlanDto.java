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
    @ApiModelProperty(value = "일정 id")
    private Long id;

    @ApiModelProperty(value = "일정 이름")
    private String name;

    @ApiModelProperty(value = "도착 장소 이름")
    private String arrivalName;

    @ApiModelProperty(value = "도착 위도")
    private Double arrivalLat;

    @ApiModelProperty(value = "도착 경도")
    private Double arrivalLng;

    @ApiModelProperty(value = "도착 시간")
    private LocalDateTime arrivalAt;

    @ApiModelProperty(value = "메모")
    private String memo;

    @ApiModelProperty(value = "챙길 물건들")
    private String belongings;
}
