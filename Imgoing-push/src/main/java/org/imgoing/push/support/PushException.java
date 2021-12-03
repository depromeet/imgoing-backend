package org.imgoing.push.support;

import lombok.Getter;

@Getter
public class PushException extends RuntimeException {
    private final PushError error;

    public PushException(PushError error) {
        super(error.getDesc());
        this.error = error;
    }

    public PushException(PushError error, String message) {
        super(error.getDesc() + " : " + message);
        this.error = error;
    }

    public PushException(PushError error, Throwable cause) {
        super(error.getDesc(), cause);
        this.error = error;
    }
}