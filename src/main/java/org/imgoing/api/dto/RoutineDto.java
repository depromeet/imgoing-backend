package org.imgoing.api.dto;

import io.swagger.annotations.ApiModel;
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
@ApiModel(value = "루틴 모델")
public class RoutineDto {
    @ApiModelProperty(value = "루틴 이름")
    private String name;

    @SuperBuilder
    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @ApiModel(value = "루틴 생성 모델")
    public static class Create extends RoutineDto {
        @ApiModelProperty(value = "준비항목 목록 Id")
        private List<Long> taskIdList;
    }

    @SuperBuilder
    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @ApiModel(value = "루틴 조회 모델")
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
    @ApiModel(value = "루틴 수정 모델")
    public static class Update extends RoutineDto.Create {
        @ApiModelProperty(value = "루틴 id")
        private Long id;
    }
}
