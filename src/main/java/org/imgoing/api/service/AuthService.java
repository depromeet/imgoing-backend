package org.imgoing.api.service;

import lombok.RequiredArgsConstructor;
import org.imgoing.api.domain.entity.CertificateAuthority;
import org.imgoing.api.domain.entity.User;
import org.imgoing.api.domain.verifier.KakaoTokenVerifier;
import org.imgoing.api.domain.vo.TokenPayload;
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
    User createUser(String email, String name, String profile) {
        User willSaveUser = User.builder()
                .email(email)
                .name(name)
                .profile(profile)
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
        String email = socialLoginRequest.getEmail();
        String name = socialLoginRequest.getName();
        String profile = socialLoginRequest.getProfile();
        User user = userRepository.findByEmail(socialLoginRequest.getEmail()).orElse(createUser(email, name, profile));
        return ca.makeToken(new TokenPayload(user.getId(), user.getEmail()));
    }
}
