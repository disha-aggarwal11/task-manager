package com.disha.taskmanager.service;

import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    private final JavaMailSender mailSender;

    public EmailService(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    public void sendPasswordResetEmail(
            String to,
            String token
    ) {

        String resetLink =
                "http://localhost:8080/auth/reset-password/" + token;

        SimpleMailMessage message =
                new SimpleMailMessage();

        message.setTo(to);

        message.setSubject("Password Reset");

        message.setText(
                "Click the link below to reset your password:\n\n"
                        + resetLink
                        + "\n\nThis link expires in 15 minutes."
        );

        mailSender.send(message);
    }
}