package org.imgoing.api.dto.plantask;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Builder;
import lombok.NoArgsConstructor;
import org.imgoing.api.domain.entity.Plantask;
import org.imgoing.api.dto.task.TaskDto;
import org.imgoing.api.mapper.TaskMapper;

import java.util.ArrayList;
import java.util.List;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ApiModel(value = "준비항목 구성 조회 모델")
public class PlantaskRead {
    @ApiModelProperty(value = "준비항목 Id")
    private Long id;

    @ApiModelProperty(value = "플랜 id")
    private Long planId;

    @ApiModelProperty(value = "플랜 모델")
    private TaskDto task;

    public PlantaskRead(Plantask plantask, TaskMapper taskMapper) {
        this.id = plantask.getId();
        if (plantask.getPlan() != null) {
            this.planId = plantask.getPlan().getId();
        }
        this.task = taskMapper.toDto(plantask.getTask());
    }
}
