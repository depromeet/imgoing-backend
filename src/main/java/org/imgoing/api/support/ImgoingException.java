package org.imgoing.api.support;

public class ImgoingException extends RuntimeException {
    private final ImgoingError error;

    public ImgoingException(ImgoingError error) {
        super(error.getDesc());
        this.error = error;
    }

    public ImgoingException(ImgoingError error, String message) {
        super(error.getDesc() + " : " + message);
        this.error = error;
    }

    public ImgoingException(ImgoingError error, Throwable cause) {
        super(error.getDesc(), cause);
        this.error = error;
    }
}