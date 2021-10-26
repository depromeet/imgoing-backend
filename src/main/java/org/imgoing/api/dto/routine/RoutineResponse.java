package org.imgoing.api.dto.routine;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.imgoing.api.dto.RoutinetaskDto;

import java.util.List;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ApiModel(value = "루틴 응답 모델")
public class RoutineResponse {
    @ApiModelProperty(value = "루틴 id")
    private Long id;

    @ApiModelProperty(value = "루틴 이름")
    private String name;

    @ApiModelProperty(value = "준비항목 목록")
    private List<RoutinetaskDto.Read> routinetasks;
}
