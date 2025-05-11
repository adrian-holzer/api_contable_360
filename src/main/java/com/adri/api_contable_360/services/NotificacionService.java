package com.adri.api_contable_360.services;

import com.adri.api_contable_360.models.AsignacionVencimiento;
import jakarta.mail.MessagingException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

@Service
public class NotificacionService {


    @Value("${spring.mail.host}")
    String host ;

    @Value("${spring.mail.port}")
    String port ;

    @Value("${spring.mail.username}")
    String username ;

    @Value("${spring.mail.password}")
    String password ;

    private  EmailService emailService;


    public NotificacionService(EmailService emailService) {
        this.emailService = emailService;

    }


}
