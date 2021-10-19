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
<<<<<<< HEAD:src/main/java/org/imgoing/api/dto/PresetDto.java
public class PresetDto {
    @ApiModelProperty(value = "프리셋 id")
    private Long id;

    @ApiModelProperty(value = "프리셋 이름")
=======
public class RoutineDto {
    @ApiModelProperty(value = "루틴 이름")
>>>>>>> 94c44d1 (Fix : 루틴 생성 및 조회 Dto 분리, 속성명 변경):src/main/java/org/imgoing/api/dto/RoutineDto.java
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
        private List<TaskDto> tasks;
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
