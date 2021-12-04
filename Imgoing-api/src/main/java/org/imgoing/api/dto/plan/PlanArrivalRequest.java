package org.imgoing.api.dto.plan;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class PlanArrivalRequest {
    private Boolean isUserLate;
    private LocalDateTime actualArrivalAt;
}
