package org.imgoing.push.service;

import com.google.auth.oauth2.GoogleCredentials;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.imgoing.push.dto.FcmTokenMessage;
import org.imgoing.push.dto.FcmTopicMessage;
import org.imgoing.push.dto.Notification;
import org.imgoing.push.support.FcmProperties;
import org.imgoing.push.support.ReactorErrorConsumer;
import org.imgoing.push.support.WebClientErrorConsumer;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.io.IOException;
import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class FcmServerProtocolClient {
    private final FcmProperties settings;
    private final WebClient webClient;
    private final ReactorErrorConsumer reactorErrorConsumer;
    private final WebClientErrorConsumer webClientErrorConsumer;

    private String getAccessToken() throws IOException {
        String serviceAccountPath = settings.getServiceAccountPath();
        log.info("serviceAccountFile을 {}에서 로딩", serviceAccountPath);

        GoogleCredentials googleCredentials = GoogleCredentials
                .fromStream(new ClassPathResource(serviceAccountPath).getInputStream())
                .createScoped(List.of("https://www.googleapis.com/auth/cloud-platform"));

        googleCredentials.refreshIfExpired();
        return googleCredentials.getAccessToken().getTokenValue();
    }

    public void sendByToken(List<String> targetTokens, String title, String body) throws IOException {
        String authorization = "Bearer " + getAccessToken();
        String apiUrl = settings.getApiUrl();
        long startTime = System.currentTimeMillis();

        Flux<FcmTokenMessage> fcmMessageFlux = Flux.fromIterable(targetTokens)
                .map(targetToken -> makeTokenMessage(targetToken, title, body))
                .doOnError(reactorErrorConsumer.create);

        fcmMessageFlux.subscribe(fcmTokenMessage -> webClient
                .post()
                .uri(apiUrl)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .header(HttpHeaders.AUTHORIZATION, authorization)
                .bodyValue(fcmTokenMessage)
                .retrieve()
                .onStatus(HttpStatus::is4xxClientError, webClientErrorConsumer.onStatusClient)
                .onStatus(HttpStatus::is5xxServerError, webClientErrorConsumer.onStatusServer)
                .bodyToFlux(String.class)
                .subscribe(res -> log.info("{}", res), webClientErrorConsumer.subscribe)
        );

        log.info("end: " + (System.currentTimeMillis() - startTime) + "sec");
    }

    public void sendByTopic(String topic, String title, String body) throws IOException {
        String authorization = "Bearer " + getAccessToken();
        String apiUrl = settings.getApiUrl();
        long startTime = System.currentTimeMillis();

        Mono<FcmTopicMessage> fcmMessageMono = Mono.just(makeTopicMessage(topic, title, body))
                .doOnError(reactorErrorConsumer.create);

        webClient
                .post()
                .uri(apiUrl)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .header(HttpHeaders.AUTHORIZATION, authorization)
                .body(fcmMessageMono, FcmTopicMessage.class)
                .retrieve()
                .onStatus(HttpStatus::is4xxClientError, webClientErrorConsumer.onStatusClient)
                .onStatus(HttpStatus::is5xxServerError, webClientErrorConsumer.onStatusServer)
                .bodyToMono(String.class)
                .subscribe(res -> log.info("{}", res), webClientErrorConsumer.subscribe);

        log.info("end: " + (System.currentTimeMillis() - startTime) + "sec");
    }

    private FcmTokenMessage makeTokenMessage(String token, String title, String body) {
        return FcmTokenMessage.builder()
                .message(FcmTokenMessage.Message.builder()
                        .token(token)
                        .notification(Notification.builder()
                                .title(title)
                                .body(body)
                                .image(null)
                                .build()
                        )
                        .build()
                )
                .validate_only(false)
                .build();
    }

    private FcmTopicMessage makeTopicMessage(String topic, String title, String body) {
        return FcmTopicMessage.builder()
                .message(FcmTopicMessage.Message.builder()
                        .topic(topic)
                        .notification(Notification.builder()
                                .title(title)
                                .body(body)
                                .image(null)
                                .build()
                        )
                        .build()
                )
                .validate_only(false)
                .build();
    }
}
