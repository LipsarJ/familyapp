package org.example.websocket;

import lombok.RequiredArgsConstructor;
import org.example.messaging.InvitationEvent;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class NotificationService {
    private final SimpMessagingTemplate messagingTemplate;

    public void sendInvitationNotification(Long userId, InvitationEvent event) {
        messagingTemplate.convertAndSendToUser(userId.toString(), "/topic/invitations", event);
    }
}
