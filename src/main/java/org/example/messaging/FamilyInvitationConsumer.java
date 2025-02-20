package org.example.messaging;

import lombok.RequiredArgsConstructor;
import org.example.config.RabbitMQConfig;
import org.example.service.EmailService;
import org.example.service.InvitationService;
import org.example.websocket.NotificationService;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class FamilyInvitationConsumer {
    private final InvitationService invitationService;
    private final EmailService emailService;
    private final NotificationService notificationService;


    @RabbitListener(queues = RabbitMQConfig.QUEUE_NAME)
    public void handleInvitation(InvitationEvent event) {
        invitationService.createInvitation(event.getUserId(), event.getFamilyId());

        emailService.sendInvitationEmail(event.getUserEmail(), event.getFamilyName());

        notificationService.sendInvitationNotification(event.getUserId(), event);
    }
}
