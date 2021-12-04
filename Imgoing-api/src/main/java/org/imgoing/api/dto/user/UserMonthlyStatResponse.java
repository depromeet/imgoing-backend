package org.imgoing.api.dto.user;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
public class UserMonthlyStatResponse {
    private long totalPlansCount;
    private long latePlansCount;
}
