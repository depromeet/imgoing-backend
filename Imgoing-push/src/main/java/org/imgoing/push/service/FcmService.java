package org.imgoing.push.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.auth.oauth2.GoogleCredentials;
import lombok.RequiredArgsConstructor;
import org.imgoing.push.dto.FcmMessage;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.io.IOException;
import java.util.List;

@Component
@RequiredArgsConstructor
public class FcmService {
    private final String API_URL = "https://fcm.googleapis.com/v1/projects/dpm-imgoing/messages:send";
    private final WebClient webClient;
    private final ObjectMapper objectMapper;

    private String getAccessToken() throws IOException {
        String firebaseConfigPath = "firebase/dpm-imgoing-firebase-adminsdk-egokj-7aa4ab4857.json";

        GoogleCredentials googleCredentials = GoogleCredentials
                .fromStream(new ClassPathResource(firebaseConfigPath).getInputStream())
                .createScoped(List.of("https://www.googleapis.com/auth/cloud-platform"));

        googleCredentials.refreshIfExpired();
        return googleCredentials.getAccessToken().getTokenValue();
    }

    public void sendMessageTo(String targetToken, String title, String body) throws IOException {
        FcmMessage fcmMessage = makeMessage(targetToken, title, body);
        Mono<FcmMessage> messageMono = Mono.just(fcmMessage);

        Mono<JsonNode> response = webClient.mutate()
                .build()
                .post()
                .uri(API_URL)
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + getAccessToken())
                .body(messageMono, FcmMessage.class)
                .retrieve()
                .bodyToMono(String.class)
                .map(s -> {
                    try {
                        return objectMapper.readTree(s);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    return null;
                });

        response.subscribe(res -> {
            System.out.println(res.asText());
        }, e -> {
            System.out.println(e);
        });
    }

    private FcmMessage makeMessage(String targetToken, String title, String body) throws JsonProcessingException {
        return FcmMessage.builder()
                .message(FcmMessage.Message.builder()
                        .token(targetToken)
                        .notification(FcmMessage.Notification.builder()
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
