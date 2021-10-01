package org.imgoing.api.application.handler;

import lombok.extern.slf4j.Slf4j;
import org.imgoing.api.support.ImgoingError;
import org.imgoing.api.support.ImgoingException;
import org.imgoing.api.support.ImgoingResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(ImgoingException.class)
    public ResponseEntity<ImgoingResponse<?>> imgoingException(ImgoingException e) {
        ImgoingError error = e.getError();
        log.error("ImgoingException : " + error.getDesc());

        return ResponseEntity
                .status(error.getStatus())
                .body(new ImgoingResponse<>(error.getStatus(), e.getMessage()));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ImgoingResponse<?>> methodArgumentNotValidException(MethodArgumentNotValidException e) {
        log.error("MethodArgumentNotValidException : " + e.getMessage());
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(new ImgoingResponse<>(HttpStatus.BAD_REQUEST, e.getMessage()));
    }

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<ImgoingResponse<?>> unknownException(Exception e) {
        log.error("UnknownException : " + e.getMessage());
        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new ImgoingResponse<>(HttpStatus.INTERNAL_SERVER_ERROR, ImgoingError.UNKNOWN_ERROR.getDesc()));
    }
}
