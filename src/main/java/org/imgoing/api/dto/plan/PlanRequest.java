package org.imgoing.api.dto.plan;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import org.hibernate.validator.constraints.Length;
import org.imgoing.api.dto.task.TaskDto;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.List;

@SuperBuilder
@Getter
@NoArgsConstructor
@ApiModel(value = "악속 정보", description = "약속 모델")
public class PlanRequest {
    @ApiModelProperty(value = "일정 id")
    private Long id;

    @NotBlank(message = "일정 이름은 필수값 입니다.")
    @ApiModelProperty(required = true, value = "일정 이름", example = "유나랑 약속")
    private String name;

    @NotBlank(message = "출발 장소 이름은 필수값 입니다.")
    @ApiModelProperty(required = true, value = "출발 장소 이름", example = "서울대입구역 9번 출구")
    private String departureName;

    @NotNull(message = "출발 위도는 필수값 입니다.")
    @ApiModelProperty(required = true, value = "출발 장소 위도", example = "37.481254")
    private Double departureLat;

    @NotNull(message = "출발 경도는 필수값 입니다.")
    @ApiModelProperty(required = true, value = "출발 장소 경도", example = "126.953228")
    private Double departureLng;

    @NotBlank(message = "도착 장소 이름은 필수값 입니다.")
    @ApiModelProperty(required = true, value = "도착 장소 이름", example = "홍대입구역 2번 출구")
    private String arrivalName;

    @NotNull(message = "도착 위도는 필수값 입니다.")
    @ApiModelProperty(required = true, value = "도착 장소 위도", example = "37.556932")
    private Double arrivalLat;

    @NotNull(message = "도착 경도는 필수값 입니다.")
    @ApiModelProperty(required = true, value = "도착 장소 경도", example = "126.923812")
    private Double arrivalLng;

    @NotNull(message = "도착 시간은 필수값 입니다.")
    @ApiModelProperty(required = true, value = "도착 시간", example = "2021-12-15 15:46:54")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Seoul")
    private LocalDateTime arrivalAt;

    @Length(max = 100, message = "메모는 최대 100자 까지 입니다.")
    @ApiModelProperty(value = "메모", example = "편의점 들러서 물 사기")
    private String memo;

    @ApiModelProperty(value = "챙길 물건들", example = "보조배터리, 고데기")
    private String belongings;

    @ApiModelProperty(value = "준비항목")
    private List<TaskDto> task;
}