package org.imgoing.push.consumer;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.imgoing.push.dto.FcmTopicDto;
import org.imgoing.push.service.FcmServerProtocolClient;
import org.springframework.amqp.AmqpRejectAndDontRequeueException;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class TopicConsumer {
    private final FcmServerProtocolClient fcmServerProtocolClient;

    @RabbitListener(queues = "${fcm.send.topic.queue}",
            containerFactory = "customRabbitListenerContainerFactory")
    public void send(final FcmTopicDto fcmTopicDto) {
        try {
            try {
                fcmServerProtocolClient.sendByTopic(fcmTopicDto.getTopic(),
                        fcmTopicDto.getNotification().getTitle(),
                        fcmTopicDto.getNotification().getBody()
                );
            } catch (Exception e) {
                throw new AmqpRejectAndDontRequeueException(e);
            }
        } catch (Exception e) {
            throw new AmqpRejectAndDontRequeueException(e);
        }
    }
}
