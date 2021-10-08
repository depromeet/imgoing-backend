package org.imgoing.api.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class TokenVerifiedResponse {
    private String email;
    private boolean verified;
    private String name;

    @Builder
    public TokenVerifiedResponse(String email, boolean verified, String name) {
        this.email = email;
        this.verified = verified;
        this.name = name;
    }
}
