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
    @SuperBuilder
    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Create {
        @ApiModelProperty(value = "준비항목 목록 Id")
        private List<Long> subtaskIdList;
    }

    @SuperBuilder
    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Read {
        @ApiModelProperty(value = "루틴 Id")
        private Long id;

        @ApiModelProperty(value = "준비항목 모델")
        private List<SubtaskDto> subtaskList;
    }
}
