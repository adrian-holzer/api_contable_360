package com.adri.api_contable_360.services;

import jakarta.activation.DataHandler;
import jakarta.mail.*;
import jakarta.mail.internet.*;
import jakarta.mail.util.ByteArrayDataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.List;
import java.util.Properties;

@Service
public class EmailService {

    private final JavaMailSender mailSender;


    @Value("${spring.mail.username}")
    private String sender;

    @Autowired
    public EmailService(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }


    public void sendPlainTextEmail(String host, int port, String username, String password, String to, String subject, String text) throws MessagingException {
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, "utf-8");
        message.setFrom(username);
        helper.setTo(to);
        helper.setSubject(subject);
        helper.setText(text, false);
        mailSender.send(message);
    }

    public void enviarEmailConAdjuntos(String host, int port, String username, String password, String to, String subject, String body, List<MultipartFile> archivosAdjuntos) throws MessagingException {
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true, StandardCharsets.UTF_8.name());
        message.setFrom(username);
        helper.setTo(to);
        helper.setSubject(subject);
        helper.setText(body, false);

        if (archivosAdjuntos != null && !archivosAdjuntos.isEmpty()) {
            for (MultipartFile archivo : archivosAdjuntos) {
                try {
                    helper.addAttachment(archivo.getOriginalFilename(), archivo);
                } catch (MessagingException e) {
                    // Manejar la excepción al adjuntar el archivo
                    e.printStackTrace();
                    throw new MessagingException("Error al adjuntar el archivo: " + archivo.getOriginalFilename(), e);
                }
            }
        }
        mailSender.send(message);
    }


}
