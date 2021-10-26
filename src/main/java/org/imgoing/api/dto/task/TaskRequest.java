package org.imgoing.api.dto.task;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.Max;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@ApiModel(value = "준비항목 요청 모델")
public class TaskRequest {
    @NotBlank(message = "{task.notBlank.name}")
    @Length(max = 50, message = "{task.length.name}")
    @ApiModelProperty(required = true, value = "준비항목 이름", example = "준비항목 1")
    private String name;

    @NotNull(message = "{task.notNull.time}")
    @Positive (message = "{task.positive.time}")
    @Max(value = Integer.MAX_VALUE, message = "{task.max.time}")
    @ApiModelProperty(required = true, value = "준비항목 시간", example = "20")
    private Integer time;

    @NotNull(message = "{task.notNull.isBookmarked}")
    @ApiModelProperty(required = true, value = "준비항목 북마크 여부", example = "true")
    private Boolean isBookmarked;
}
