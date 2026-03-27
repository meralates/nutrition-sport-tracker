package com.example.nutritionsporttracker.service;

import jakarta.mail.internet.MimeMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    private final JavaMailSender javaMailSender;

    public EmailService(JavaMailSender javaMailSender) {
        this.javaMailSender = javaMailSender;
    }

    // ✅ HTML mail
    public void sendHtmlEmail(String to, String subject, String html) {
        try {
            MimeMessage mime = javaMailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mime, "UTF-8");
            helper.setTo(to);
            helper.setSubject(subject);
            helper.setText(html, true); // true => HTML
            helper.setFrom("meralates2002@gmail.com");
             javaMailSender.send(mime);
        } catch (Exception e) {
            throw new RuntimeException("Email gönderilemedi: " + e.getMessage(), e);
        }
    }
}