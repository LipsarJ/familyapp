package org.example.messaging;

import lombok.RequiredArgsConstructor;
import org.example.config.RabbitMQConfig;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class FamilyInvitationProducer {
    private final RabbitTemplate rabbitTemplate;

    public void sendInvitationEvent(InvitationEvent event) {
        rabbitTemplate.convertAndSend(RabbitMQConfig.EXCHANGE_NAME, RabbitMQConfig.ROUTING_KEY, event);
    }
}
