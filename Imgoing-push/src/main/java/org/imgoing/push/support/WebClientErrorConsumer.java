package org.imgoing.push.support;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.reactive.function.client.ClientResponse;
import reactor.core.publisher.Mono;

import java.util.function.Consumer;
import java.util.function.Function;

@Slf4j
@Component
public class WebClientErrorConsumer {
    public final Consumer<? super Throwable> subscribe = (e) -> {
        e.printStackTrace();
        log.error(e.getMessage());
        throw new PushException(PushError.SUBSCRIBE_ERROR, e);
    };

    public final Function<ClientResponse, Mono<? extends Throwable>> onStatusClient = (cr) ->
            Mono.error(new HttpClientErrorException(cr.statusCode(), cr.statusCode().getReasonPhrase()));

    public final Function<ClientResponse, Mono<? extends Throwable>> onStatusServer = (cr) ->
            Mono.error(new HttpServerErrorException(cr.statusCode(), cr.statusCode().getReasonPhrase()));
}
