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
public class TaskDto {
    @ApiModelProperty(value = "준비항목 id")
    private Long id;

    @ApiModelProperty(value = "준비항목 이름")
    private String name;

    @ApiModelProperty(value = "준비항목 시간")
    private Integer time;

    @ApiModelProperty(value = "북마크 여부")
    private Boolean isBookmarked;
}
