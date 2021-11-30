package org.imgoing.push.service;

import com.google.auth.oauth2.GoogleCredentials;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.imgoing.push.dto.FcmMessage;
import org.imgoing.push.dto.Notification;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import java.io.IOException;
import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class FcmService {
    private final String API_URL = "https://fcm.googleapis.com/v1/projects/dpm-imgoing/messages:send";
    private final WebClient webClient;

    private String getAccessToken() throws IOException {
        String firebaseConfigPath = "firebase/dpm-imgoing-firebase-adminsdk-egokj-7aa4ab4857.json";

        GoogleCredentials googleCredentials = GoogleCredentials
                .fromStream(new ClassPathResource(firebaseConfigPath).getInputStream())
                .createScoped(List.of("https://www.googleapis.com/auth/cloud-platform"));

        googleCredentials.refreshIfExpired();
        return googleCredentials.getAccessToken().getTokenValue();
    }

    public void sendToTarget(String targetToken, String title, String body) throws IOException {
        FcmMessage fcmMessage = makeMessage(targetToken, title, body);
        Mono<FcmMessage> messageMono = Mono.just(fcmMessage);

        long startTime = System.currentTimeMillis();

        webClient
                .post()
                .uri(API_URL)
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + getAccessToken())
                .body(messageMono, FcmMessage.class)
                .retrieve()
                .onStatus(status -> status.is4xxClientError()
                                || status.is5xxServerError()
                        , clientResponse ->
                                clientResponse.bodyToMono(String.class)
                                        .map(b -> new RuntimeException(b))
                )
                .bodyToMono(String.class)
                .subscribe(res -> log.debug("{}", res));

        log.info("end: " + (System.currentTimeMillis() - startTime) + "sec");
    }

    public void sendToPeople(List<String> targetTokens, String title, String body) throws IOException {
        String authorization = "Bearer " + getAccessToken();
        long startTime = System.currentTimeMillis();

        Flux.fromIterable(targetTokens)
                .flatMap(targetToken -> Mono.just(makeMessage(targetToken, title, body)))
                .map(messageMono -> webClient
                        .post()
                        .uri(API_URL)
                        .header(HttpHeaders.AUTHORIZATION, authorization)
                        .body(messageMono, FcmMessage.class)
                        .retrieve()
                        .onStatus(status -> status.is4xxClientError()
                                        || status.is5xxServerError()
                                , clientResponse ->
                                        clientResponse.bodyToMono(String.class)
                                                .map(b -> new RuntimeException(b))
                        )
                        .bodyToMono(String.class))
                .subscribeOn(Schedulers.parallel());

        log.info("end: " + (System.currentTimeMillis() - startTime) + "sec");
    }

    private FcmMessage makeMessage(String token, String title, String body) {
        return FcmMessage.builder()
                .message(FcmMessage.Message.builder()
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
}
