package org.imgoing.push.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.config.SimpleRabbitListenerContainerFactory;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Slf4j
@Configuration
public class RabbitMQConfig {
    @Value("${fcm.send.token.exchange}")
    private String tokenExchange;
    @Value("${fcm.send.token.queue}")
    private String tokenQueue;
    @Value("${fcm.send.token.dlqueue}")
    private String tokenDLQueue;

    @Value("${fcm.send.topic.exchange}")
    private String topicExchange;
    @Value("${fcm.send.topic.queue}")
    private String topicQueue;
    @Value("${fcm.send.topic.dlqueue}")
    private String topicDLQueue;

    @Value("${fcm.rabbitmq.addresses}")
    private String addresses;
    @Value("${fcm.rabbitmq.username}")
    private String username;
    @Value("${fcm.rabbitmq.password}")
    private String password;

    @Bean
    public CachingConnectionFactory connectionFactory() {
        CachingConnectionFactory connectionFactory = new CachingConnectionFactory();
        connectionFactory.setUri(addresses);
        connectionFactory.setUsername(username);
        connectionFactory.setPassword(password);
        return connectionFactory;
    }

    @Bean
    public AmqpAdmin amqpAdmin() {
        return new RabbitAdmin(connectionFactory());
    }

    @Bean
    public DirectExchange tokenExchange() {
        return new DirectExchange(tokenExchange, true, false);
    }

    @Bean
    public DirectExchange topicExchange() {
        return new DirectExchange(topicExchange, true, false);
    }

    @Bean
    public Queue tokenQueue() {
        return QueueBuilder.durable(tokenQueue)
                .deadLetterExchange("")
                .deadLetterRoutingKey(tokenDLQueue)
                .build();
    }

    @Bean
    public Queue topicQueue() {
        return QueueBuilder.durable(topicQueue)
                .deadLetterExchange("")
                .deadLetterRoutingKey(topicDLQueue)
                .build();
    }

    @Bean
    public Queue tokenDLQueue() {
        return new Queue(tokenDLQueue, true, false, false);
    }

    @Bean
    public Queue topicDLQueue() {
        return new Queue(topicDLQueue, true, false, false);
    }

    @Bean
    public Binding topicBinding() {
        return BindingBuilder.bind(topicQueue()).to(topicExchange()).with(topicQueue);
    }

    @Bean
    public Binding tokenBinding() {
        return BindingBuilder.bind(tokenQueue()).to(tokenExchange()).with(tokenQueue);
    }

    @Bean
    public SimpleRabbitListenerContainerFactory customRabbitListenerContainerFactory(ConnectionFactory connectionFactory) {
        final SimpleRabbitListenerContainerFactory factory = new SimpleRabbitListenerContainerFactory();
        factory.setConnectionFactory(connectionFactory);
        factory.setConnectionFactory(connectionFactory);
        factory.setErrorHandler(t -> {
            log.error("{}", t.getCause());
            log.error("{}", t.getMessage());
        });
        factory.setDefaultRequeueRejected(false); // true일 경우 리스너에서 예외가 발생시 다시 큐에 메시지가 쌓이게 된다.(예외상황 해결안될 시 무한루프)
        factory.setMessageConverter(Jackson2JsonMessageConverter());
        // 예외 발생시 recover할 수 있는 옵션, 위 에러 핸들러와 잘 구분해서 사용해야할 듯
        // 위 핸들러와 적용 순서등도 테스트가 필요 (혹은 둘다 동시에 적용되나?)
//        factory.setAdviceChain(
//                RetryInterceptorBuilder
//                .stateless()
//                .maxAttempts(3) //최대 재시도 횟수
//                .recoverer() //recover handler
//                .backOffOptions(2000, 4, 10000) //2초 간격으로, 4번, 최대 10초 내에 재시도
//                .build()
//        );
        return factory;
    }

    @Bean
    RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory, MessageConverter messageConverter) {
        RabbitTemplate rabbitTemplate = new RabbitTemplate();
        rabbitTemplate.setConnectionFactory(connectionFactory);
        rabbitTemplate.setMessageConverter(messageConverter);
        return rabbitTemplate;
    }

    @Bean
    public Jackson2JsonMessageConverter Jackson2JsonMessageConverter(){
        return new Jackson2JsonMessageConverter();
    }
}