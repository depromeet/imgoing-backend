package org.imgoing.api.domain.vo;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class TokenPayload {
    private final long id;
    private final String email;

    public boolean isSame(TokenPayload anotherTokenPayload) {
        return id == anotherTokenPayload.getId() && email == anotherTokenPayload.getEmail();
    }
}
