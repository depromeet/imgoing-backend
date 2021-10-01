package org.imgoing.api.support;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum ImgoingError {
    UNKNOWN_ERROR("알 수 없는 에러", HttpStatus.INTERNAL_SERVER_ERROR),
    NOT_LOGIN("비 로그인 사용자", HttpStatus.UNAUTHORIZED),
    ENTITY_NOT_FOUND("엔터티를 찾을 수 없습니다.", HttpStatus.INTERNAL_SERVER_ERROR),
    BAD_REQUEST("사용자의 입력이 잘못되었습니다.", HttpStatus.BAD_REQUEST),
    NOT_PERMITTED("권한이 없는 유저 입니다.", HttpStatus.FORBIDDEN),
    DUPLICATE("중복 입력입니다.", HttpStatus.BAD_REQUEST),
    UNEXPECTED_VALUE("예상하지 못한 값입니다.", HttpStatus.INTERNAL_SERVER_ERROR);

    private final String desc;
    private final HttpStatus status;

    ImgoingError(String desc, HttpStatus status) {
        this.desc = desc;
        this.status = status;
    }
}