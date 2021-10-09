package org.imgoing.api.domain.vo;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class TokenPayload {
    private long id;
    private String email;

    public boolean isSame(TokenPayload anotherTokenPayload) {
        return id == anotherTokenPayload.getId() && email == anotherTokenPayload.getEmail();
    }
}
