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
public class PresetDto {
    @ApiModelProperty(value = "프리셋 id")
    private Long id;

    @ApiModelProperty(value = "프리셋 이름")
    private String name;
}
