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
@ApiModel(value = "준비항목 모델")
public class TaskDto {
    @ApiModelProperty(value = "준비항목 이름")
    private String name;

    @ApiModelProperty(value = "준비항목 시간")
    private Integer time;

    @ApiModelProperty(value = "북마크 여부", example = "false")
    private Boolean isBookmarked;

    @SuperBuilder
    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @ApiModel(value = "준비항목 조회 모델")
    public static class Read extends TaskDto {
        @ApiModelProperty(value = "준비항목 id")
        private Long id;
    }

    @SuperBuilder
    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @ApiModel(value = "준비항목 수정 모델")
    public static class Update extends TaskDto {
        @ApiModelProperty(value = "준비항목 id")
        private Long id;
    }
}
