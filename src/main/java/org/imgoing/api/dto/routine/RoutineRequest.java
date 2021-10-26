package org.imgoing.api.dto.routine;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@ApiModel(value = "루틴 요청 모델")
public class RoutineRequest {
    @NotBlank(message = "{routine.notBlank.name}")
    @Length(max = 50, message = "{routine.length.name}")
    @ApiModelProperty(required = true, value = "루틴 이름", example = "루틴 1")
    private String name;

    @NotNull(message = "{routine.notNull.taskIdList}")
    @ApiModelProperty(required = true, value = "준비항목 id 리스트", example = "[1, 2, 3]")
    private List<Long> taskIdList;
}
