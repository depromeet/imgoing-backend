package org.imgoing.api.dto.plan;


import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import org.imgoing.api.dto.task.TaskDto;

import java.time.LocalDateTime;
import java.util.List;

@SuperBuilder
@Getter
@Setter
@NoArgsConstructor
public class PlanDto {
    private Long id;

    private String name;

    private String departureName;

    private Double departureLat;

    private Double departureLng;

    private String arrivalName;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime arrivalAt;

    private Double arrivalLat;

    private Double arrivalLng;

    private String memo;

    private String belongings;

    private List<TaskDto> task;
}
