package org.imgoing.push.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.imgoing.push.dto.FcmMessage;
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
    public void sendMessageByTarget(@RequestBody FcmMessage request) throws IOException {
        fcmService.sendMessageTo(request.getMessage().getToken(),
                request.getMessage().getNotification().getTitle(),
                request.getMessage().getNotification().getBody()
        );
    }
}
