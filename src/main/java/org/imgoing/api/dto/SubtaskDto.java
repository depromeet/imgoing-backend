package org.imgoing.api.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@SuperBuilder
@Getter
@Setter
@NoArgsConstructor
public class SubtaskDto {
    @ApiModelProperty(value = "세부업무 id")
    private Long id;

    @ApiModelProperty(value = "세부업무 이름")
    private String name;

    @ApiModelProperty(value = "세부업무 시간")
    private Integer time;
}
