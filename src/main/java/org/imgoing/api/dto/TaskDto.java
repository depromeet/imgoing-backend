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
public class TaskDto {
    @ApiModelProperty(value = "일정 id")
    private Long id;
    @ApiModelProperty(value = "일정 이름")
    private String name;
    @ApiModelProperty(value = "일정 시간")
    private Integer time;
}