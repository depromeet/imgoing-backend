package org.imgoing.api.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@SuperBuilder
@Getter
@Setter
@NoArgsConstructor
@ApiModel(value = "루틴 - 준비항목 모델")
public class RoutinetaskDto {
    @SuperBuilder
    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @ApiModel(value = "루틴 - 준비항목 조회 모델")
    public static class Read extends RoutinetaskDto {
        @ApiModelProperty(value = "준비항목 모델")
        private TaskDto.Read task;
    }
}