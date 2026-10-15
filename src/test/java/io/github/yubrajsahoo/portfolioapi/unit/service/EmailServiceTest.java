/*
 *
 *  * Copyright (c) 2026 Yubraj Sahoo. All rights reserved.
 *
 */

package io.github.yubrajsahoo.portfolioapi.unit.service;

import io.github.yubrajsahoo.portfolioapi.dto.ContactReqDto;
import io.github.yubrajsahoo.portfolioapi.service.EmailService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mail.MailSendException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import java.util.Objects;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@SpringBootTest
@ActiveProfiles("unit")
@DisplayName("Unit: Email Service Operations")
class EmailServiceTest {

    @MockitoBean
    private JavaMailSender mailSender;

    @Autowired
    private EmailService emailService;

    @Test
    @DisplayName("Should successfully send contact notification email")
    void sendContactNotificationEmail_Success() {
        ContactReqDto reqDto = ContactReqDto.builder()
                .name("Alex Smith")
                .email("alex@example.com")
                .message("I love your portfolio and would like to connect.")
                .build();

        doNothing().when(mailSender).send(any(SimpleMailMessage.class));

        emailService.sendContactNotificationEmail(reqDto);

        ArgumentCaptor<SimpleMailMessage> captor = ArgumentCaptor.forClass(SimpleMailMessage.class);
        verify(mailSender, times(1)).send(captor.capture());

        SimpleMailMessage sentMessage = captor.getValue();
        assertNotNull(sentMessage);
        assertTrue(Objects.requireNonNull(sentMessage.getTo())[0].contains("example.com") || Objects.requireNonNull(sentMessage.getTo())[0].contains("gmail.com"));
        assertEquals("New Contact Message from Alex Smith", sentMessage.getSubject());
        assertTrue(Objects.requireNonNull(sentMessage.getText()).contains("Alex Smith"));
        assertTrue(sentMessage.getText().contains("alex@example.com"));
        assertTrue(sentMessage.getText().contains("I love your portfolio and would like to connect."));
    }

    @Test
    @DisplayName("Should handle MailException gracefully without throwing")
    void sendContactNotificationEmail_HandlesException() {
        ContactReqDto reqDto = ContactReqDto.builder()
                .name("Alex Smith")
                .email("alex@example.com")
                .message("Test message")
                .build();

        doThrow(new MailSendException("SMTP error")).when(mailSender).send(any(SimpleMailMessage.class));

        assertDoesNotThrow(() -> emailService.sendContactNotificationEmail(reqDto));
        verify(mailSender, times(1)).send(any(SimpleMailMessage.class));
    }
}
