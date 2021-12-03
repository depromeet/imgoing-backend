package org.imgoing.push.support;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum PushError {
    UNKNOWN_ERROR("알 수 없는 에러입니다.", HttpStatus.INTERNAL_SERVER_ERROR),
    SUBSCRIBE_ERROR("구독 중 에러가 발생했습니다.", HttpStatus.INTERNAL_SERVER_ERROR),
    BAD_REQUEST("사용자의 입력이 잘못되었습니다.", HttpStatus.BAD_REQUEST);

    private final String desc;
    private final HttpStatus status;

    PushError(String desc, HttpStatus status) {
        this.desc = desc;
        this.status = status;
    }
}