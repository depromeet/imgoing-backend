package org.imgoing.push.consumer;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.imgoing.push.dto.FcmTokenDto;
import org.imgoing.push.service.FcmServerProtocolClient;
import org.springframework.amqp.AmqpRejectAndDontRequeueException;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class TokenConsumer {
    private final FcmServerProtocolClient fcmServerProtocolClient;

    @RabbitListener(queues = "${fcm.send.token.queue}",
            containerFactory = "customRabbitListenerContainerFactory")
    public void send(final FcmTokenDto fcmTokenDto) {
        try {
            try {
                fcmServerProtocolClient.sendByToken(fcmTokenDto.getTokens(),
                        fcmTokenDto.getNotification().getTitle(),
                        fcmTokenDto.getNotification().getBody()
                        );
            } catch (Exception e) {
                throw new AmqpRejectAndDontRequeueException(e);
            }
        } catch (Exception e) {
            throw new AmqpRejectAndDontRequeueException(e);
        }
    }
}
