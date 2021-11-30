package org.imgoing.push.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.imgoing.push.dto.FcmDto;
import org.imgoing.push.service.FcmService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RequiredArgsConstructor
@RestController
@Api(tags = "푸시 관련 API")
@RequestMapping("/push/v2")
public class PushController {
    private final FcmService fcmService;

    @ApiOperation(value = "특정 유저에게 푸시 전송")
    @PostMapping
    public void sendToTarget(@RequestBody FcmDto request) throws IOException {
        fcmService.sendToTarget(request.getTokens().get(0),
                request.getNotification().getTitle(),
                request.getNotification().getBody()
        );
    }

    @ApiOperation(value = "여러 유저에게 푸시 전송")
    @PostMapping("/people")
    public void sendToPeople(@RequestBody FcmDto request) throws IOException {
        fcmService.sendToPeople(request.getTokens(),
                request.getNotification().getTitle(),
                request.getNotification().getBody()
        );
    }
}
