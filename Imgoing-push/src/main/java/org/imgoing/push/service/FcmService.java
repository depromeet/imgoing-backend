package org.imgoing.push.service;

import com.google.auth.oauth2.GoogleCredentials;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.imgoing.push.dto.FcmMessage;
import org.imgoing.push.dto.Notification;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;

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

    public void send(List<String> targetTokens, String title, String body) throws IOException {
        String authorization = "Bearer " + getAccessToken();
        long startTime = System.currentTimeMillis();

        Flux<FcmMessage> fcmMessageFlux = Flux.fromStream(targetTokens.stream()
                .map(targetToken -> makeMessage(targetToken, title, body))
        );

        fcmMessageFlux.subscribe(fcmMessage -> webClient
                .post()
                .uri(API_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .header(HttpHeaders.AUTHORIZATION, authorization)
                .bodyValue(fcmMessage)
                .retrieve()
                .onStatus(status -> status.is4xxClientError()
                                || status.is5xxServerError()
                        , clientResponse ->
                                clientResponse.bodyToMono(String.class)
                                        .map(b -> new RuntimeException(b))
                )
                .bodyToFlux(String.class)
                .subscribe(res -> log.info("{}", res))
        );

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
