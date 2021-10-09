package org.imgoing.api.dto.auth;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class TokenVerifyRequest {
    private final String accessToken;
}
