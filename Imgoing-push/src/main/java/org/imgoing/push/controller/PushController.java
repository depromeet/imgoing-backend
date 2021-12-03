package org.imgoing.push.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.imgoing.push.dto.FcmTokenDto;
import org.imgoing.push.dto.FcmTopicDto;
import org.imgoing.push.service.FcmServerProtocolClient;
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
    private final FcmServerProtocolClient fcmServerProtocolClient;

    @ApiOperation(value = "토큰으로 푸시 전송")
    @PostMapping
    public void sendToToken(@RequestBody FcmTokenDto request) throws IOException {
        fcmServerProtocolClient.sendByToken(request.getTokens(),
                request.getNotification().getTitle(),
                request.getNotification().getBody()
        );
    }

    @ApiOperation(value = "토픽으로 푸시 전송")
    @PostMapping("/topic")
    public void sendByTopic(@RequestBody FcmTopicDto request) throws IOException {
        fcmServerProtocolClient.sendByTopic(request.getTopic(),
                request.getNotification().getTitle(),
                request.getNotification().getBody()
        );
    }
}
