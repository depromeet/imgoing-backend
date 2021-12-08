package org.imgoing.api.dto.plantask;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@ApiModel(value = "준비항목 구성 생성 요청 모델")
public class PlantaskCreateRequest {
    @ApiModelProperty(value = "준비항목 목록 Id")
    private List<Long> taskIdList;
}
