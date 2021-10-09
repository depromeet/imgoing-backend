package org.imgoing.api.dto.auth;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public class SocialLoginRequest {
    private final String accessToken;
}
