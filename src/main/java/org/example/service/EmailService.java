package org.example.service;

import lombok.RequiredArgsConstructor;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
public class EmailService {
    private final JavaMailSender mailSender;

    public void sendInvitationEmail(String toEmail, String familyName) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(toEmail);
        message.setSubject("Приглашение в семью");
        message.setText("Вы были приглашены в семью: " + familyName + ". Зайдите в приложение, чтобы принять приглашение.");
        mailSender.send(message);
    }
}
