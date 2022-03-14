package org.imgoing.push.support;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.function.Consumer;

@Slf4j
@Component
public class ReactorErrorConsumer {
    public final Consumer<? super Throwable> create = (e) -> {
        e.printStackTrace();
        log.error(e.getMessage());
        throw new PushException(PushError.BAD_REQUEST, e);
    };
}
