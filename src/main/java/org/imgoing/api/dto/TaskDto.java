package org.imgoing.api.dto;

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
public class TaskDto {
    @ApiModelProperty(value = "루틴 id")
    private Long id;

    @ApiModelProperty(value = "루틴 이름")
    private String name;
}
