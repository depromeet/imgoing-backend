package org.imgoing.api.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.util.List;

@SuperBuilder
@Getter
@Setter
@NoArgsConstructor
public class RoutineDto {
    @ApiModelProperty(value = "루틴 이름")
    private String name;

    @SuperBuilder
    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Create extends RoutineDto {
        @ApiModelProperty(value = "준비항목 목록 Id")
        private List<Long> taskIdList;
    }

    @SuperBuilder
    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Read extends RoutineDto {
        @ApiModelProperty(value = "루틴 id")
        private Long id;

        @ApiModelProperty(value = "준비항목 목록")
        private List<RoutinetaskDto.Read> routinetasks;
    }

    @SuperBuilder
    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Update extends RoutineDto.Create {
        @ApiModelProperty(value = "루틴 id")
        private Long id;
    }
}
