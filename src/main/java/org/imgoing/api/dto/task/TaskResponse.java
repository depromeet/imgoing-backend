package org.imgoing.api.dto.task;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ApiModel(value = "준비항목 응답 모델")
public class TaskResponse {
    @ApiModelProperty(value = "준비항목 id")
    private Long id;

    @ApiModelProperty(value = "준비항목 이름")
    private String name;

    @ApiModelProperty(value = "준비항목 시간")
    private Integer time;

    @ApiModelProperty(value = "준비항목 북마크 여부")
    private Boolean isBookmarked;
}
