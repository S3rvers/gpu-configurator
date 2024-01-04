package com.amalitech.gpuconfigurator.service;

import com.amalitech.gpuconfigurator.service.email.EmailServiceImpl;
import jakarta.mail.internet.MimeMessage;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mail.javamail.JavaMailSender;
import org.thymeleaf.ITemplateEngine;

@SpringBootTest
@ExtendWith(MockitoExtension.class)
public class EmailServiceTest {

    @Autowired
    @InjectMocks
    private EmailServiceImpl emailService;

    @Mock
    private JavaMailSender javaMailSender;

    @Mock
    private ITemplateEngine templateEngine;

    @Mock
    private MimeMessage mimeMessage;

    @Test
    public void isValidEmail_shouldPassIfEmailIsValid() {
        String testEmail = "testemail@gmail.com";
        boolean isValidEmail = emailService.isValidEmail(testEmail);

        assertTrue(isValidEmail);
    }


}
