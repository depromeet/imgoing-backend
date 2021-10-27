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
    @NotBlank(message = "준비항목 이름은 필수값 입니다.")
    @Length(max = 50, message = "준비항목 이름은 최대 50자 까지 입니다.")
    @ApiModelProperty(required = true, value = "준비항목 이름", example = "준비항목 1")
    private String name;

    @NotNull(message = "시간은 null일 수 없습니다.")
    @Positive (message = "준비항목 시간은 0보다 커야 합니다.")
    @Max(value = Integer.MAX_VALUE, message = "시간의 최댓값은 2,147,483,647 입니다.")
    @ApiModelProperty(required = true, value = "준비항목 시간", example = "20")
    private Integer time;

    @NotNull(message = "준비항목 북마크 여부는 null일 수 없습니다.")
    @ApiModelProperty(required = true, value = "준비항목 북마크 여부", example = "true")
    private Boolean isBookmarked;
}
