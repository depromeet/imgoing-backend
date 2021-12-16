package org.imgoing.api.dto.task;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ApiModel(value = "준비항목 모델")
public class TaskDto {
    @ApiModelProperty(value = "준비항목 id")
    private Long id;

    @ApiModelProperty(value = "준비항목 이름")
    private String name;

    @ApiModelProperty(value = "준비항목 시간")
    private Integer time;

    @ApiModelProperty(value = "준비항목 북마크 여부")
    private Boolean isBookmarked;
}
