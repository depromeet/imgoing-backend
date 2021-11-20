package org.imgoing.api.service;

import lombok.RequiredArgsConstructor;
import org.imgoing.api.domain.entity.CertificateAuthority;
import org.imgoing.api.domain.entity.User;
import org.imgoing.api.domain.verifier.KakaoTokenVerifier;
import org.imgoing.api.domain.vo.TokenPayload;
import org.imgoing.api.dto.auth.DummyLoginRequest;
import org.imgoing.api.dto.auth.SocialLoginRequest;
import org.imgoing.api.dto.auth.TokenVerifiedResponse;
import org.imgoing.api.dto.auth.TokenVerifyRequest;
import org.imgoing.api.repository.UserRepository;
import org.imgoing.api.support.ImgoingError;
import org.imgoing.api.support.ImgoingException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final UserRepository userRepository;
    private final KakaoTokenVerifier tokenVerifier;
    private final CertificateAuthority ca;

    @Transactional(propagation = Propagation.MANDATORY)
    User createUser(String email, String name) {
        User willSaveUser = User.builder()
                .email(email)
                .nickname(name)
                .build();
        return userRepository.save(willSaveUser);
    }

    @Transactional
    public String socialLogin (SocialLoginRequest socialLoginRequest) {
        TokenVerifyRequest verifyRequest = new TokenVerifyRequest(socialLoginRequest.getAccessToken());
        TokenVerifiedResponse verifiedResponse = tokenVerifier.verify(verifyRequest);
        if (!verifiedResponse.isVerified()) {
            throw new ImgoingException(ImgoingError.BAD_REQUEST);
        }
        User user = userRepository.findByEmail(verifiedResponse.getEmail())
                .orElseGet(() -> createUser(verifiedResponse.getEmail(), verifiedResponse.getName()));
        return ca.makeToken(new TokenPayload(user.getId(), user.getEmail()));
    }

    @Transactional
    public String dummyLogin (DummyLoginRequest dummyLoginRequest) {
        User user = userRepository.findByEmail(dummyLoginRequest.getEmail())
                .orElseGet(() -> createUser(dummyLoginRequest.getEmail(), dummyLoginRequest.getNickname()));
        return ca.makeToken(new TokenPayload(user.getId(), user.getEmail()));
    }
}
