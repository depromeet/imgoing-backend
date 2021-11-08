package org.imgoing.api.dto.plan;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class PlanBookmarkDto {
    private Long id;
    private Boolean isImportant;
}
