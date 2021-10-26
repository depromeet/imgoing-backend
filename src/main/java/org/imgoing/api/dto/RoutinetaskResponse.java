package org.imgoing.api.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import org.imgoing.api.dto.task.TaskResponse;

@SuperBuilder
@Getter
@Setter
@NoArgsConstructor
@ApiModel(value = "루틴 - 준비항목 모델")
public class RoutinetaskResponse {
    @ApiModelProperty(value = "준비항목 모델")
    private TaskResponse task;
}