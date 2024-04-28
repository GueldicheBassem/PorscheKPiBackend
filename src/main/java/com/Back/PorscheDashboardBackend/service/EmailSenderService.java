package com.Back.PorscheDashboardBackend.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailSenderService {
    @Autowired
    private JavaMailSender mailSender;

    public void sendEmail(String to, String subject, String text) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(to);
        message.setFrom("Bazzemezz@gmail.com");
        message.setSubject(subject);
        message.setText(text);
        mailSender.send(message);
        System.out.println("Email has been sent to your boss bozo");
    }
}