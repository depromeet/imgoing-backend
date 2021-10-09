package org.imgoing.api.dto.auth;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public class DummyLoginRequest {
    private final String email;
    private final String nickname;
}
