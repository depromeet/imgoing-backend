package org.imgoing.api.dto.auth;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public class SocialLoginRequest {
    private final String accessToken;
    private final String email;
    private final String name;
    private final String profile;
}
