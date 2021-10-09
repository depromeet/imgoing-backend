package org.imgoing.api.domain.verifier;

import com.google.gson.Gson;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.imgoing.api.dto.auth.KakaoTokenVerifiedResponse;
import org.imgoing.api.dto.auth.TokenVerifiedResponse;
import org.imgoing.api.dto.auth.TokenVerifyRequest;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

@Slf4j
@NoArgsConstructor
@Component
public class KakaoTokenVerifier {
    private static final RestTemplate kakaoVerifyClient = new RestTemplate();
    private static final String url = "https://kapi.kakao.com/v2/user/me";
    private final TokenVerifiedResponse failResponse = TokenVerifiedResponse.builder().verified(false).build();
    private Gson gson = new Gson();

    public TokenVerifiedResponse verify (TokenVerifyRequest request) {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setBearerAuth(request.getAccessToken());
            String requestBody = "property_keys=[\"kakao_account.email\"]";
            HttpEntity<String> httpEntity = new HttpEntity<>(requestBody, headers);

            ResponseEntity<String> response = kakaoVerifyClient.postForEntity(url, httpEntity, String.class);

            if (!response.getStatusCode().is2xxSuccessful()) {
                return failResponse;
            }
            KakaoTokenVerifiedResponse verifyResponse = gson.fromJson(response.getBody(), KakaoTokenVerifiedResponse.class);
            TokenVerifiedResponse responseDto = TokenVerifiedResponse.builder()
                .email(verifyResponse.getKakaoAccountData().getEmail())
                .verified(verifyResponse.getKakaoAccountData().isEmailVerified())
                .name(verifyResponse.getKakaoAccountData().getProfile().getNickname())
                .build();
            return responseDto;
        } catch (HttpClientErrorException e) {
            return failResponse;
        }
    }
}