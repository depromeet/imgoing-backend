package org.imgoing.api.dto.routinetask;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import org.imgoing.api.dto.task.TaskDto;

@SuperBuilder
@Getter
@Setter
@NoArgsConstructor
@ApiModel(value = "루틴 - 준비항목 모델")
public class RoutinetaskDto {
    @ApiModelProperty(value = "준비항목 모델")
    private TaskDto task;
}