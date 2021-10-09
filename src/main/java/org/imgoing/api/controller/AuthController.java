package org.imgoing.api.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.imgoing.api.dto.auth.DummyLoginRequest;
import org.imgoing.api.dto.auth.SocialLoginRequest;
import org.imgoing.api.service.AuthService;
import org.imgoing.api.support.ImgoingResponse;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@Api(tags = "인가 관련 API")
@RequestMapping("/api/v1/auth")
public class AuthController {
    private final AuthService authService;

    @ApiOperation(value = "토큰 검증 및 생성")
    @PostMapping("")
    public ImgoingResponse<String> socialLogin(SocialLoginRequest request) {
        String token = authService.socialLogin(request);
        return new ImgoingResponse<>(token);
    }

    @ApiOperation(value = "테스트용 유저 생성 및 토큰 발급")
    @PostMapping("/dummy")
    public ImgoingResponse<String> dummyLogin(DummyLoginRequest request) {
        String token = authService.dummyLogin(request);
        return new ImgoingResponse<>(token);
    }
}
