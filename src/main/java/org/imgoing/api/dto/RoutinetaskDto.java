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
public class RoutinetaskDto {
    @ApiModelProperty(value = "루틴 Id")
    private Long routineId;
    @ApiModelProperty(value = "준비항목 Id")
    private Long taskId;
}

