package org.imgoing.api.dto.plantask;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Builder;
import lombok.NoArgsConstructor;
import org.imgoing.api.dto.task.TaskDto;

import java.util.List;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ApiModel(value = "준비항목 구성 조회 모델")
public class PlantaskRead {
    @ApiModelProperty(value = "루틴 Id")
    private Long id;

    @ApiModelProperty(value = "준비항목 모델")
    private List<TaskDto> taskList;
}
