package org.example.emailservice.consumers;

import org.example.emailservice.dtos.EmailFormat;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import javax.mail.Authenticator;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import java.util.Properties;

@Service
public class EmailConsumer {

    @Autowired
    private ObjectMapper objectMapper;

    @KafkaListener(topics = "sendEmail", id = "emailConsumerGroup")
    public void processEmailMessage(String message) {
        EmailFormat emailFormat;
        try {
            emailFormat = objectMapper.readValue(message, EmailFormat.class);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Failed to parse email message", e);
        }
        sendEmail(emailFormat);
    }

    private void sendEmail(EmailFormat emailFormat) {
        final String fromEmail = emailFormat.getFrom();
        final String emailPassword = "pxuviyhfqbdddoncnki";
        final String toEmail = emailFormat.getTo();

        System.out.println("Starting TLS Email");

        Properties emailProperties = new Properties();
        emailProperties.put("mail.smtp.host", "smtp.gmail.com");
        emailProperties.put("mail.smtp.port", "587");
        emailProperties.put("mail.smtp.auth", "true");
        emailProperties.put("mail.smtp.starttls.enable", "true");

        Authenticator authenticator = new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(fromEmail, emailPassword);
            }
        };

        Session emailSession = Session.getInstance(emailProperties, authenticator);
        EmailUtil.sendEmail(emailSession, toEmail, "TLS Email Testing Subject", "TLS Email Testing Body");
    }
}
