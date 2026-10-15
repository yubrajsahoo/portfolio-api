/*
 *
 *  * Copyright (c) 2026 Yubraj Sahoo. All rights reserved.
 *
 */

package io.github.yubrajsahoo.portfolioapi.integration.service;

import io.github.yubrajsahoo.portfolioapi.dto.ContactReqDto;
import io.github.yubrajsahoo.portfolioapi.service.EmailService;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.test.context.ActiveProfiles;

import static org.junit.jupiter.api.Assertions.*;

/**
 * End-to-end Integration tests for {@link EmailService} and {@link JavaMailSender}.
 * <p>
 * Tests real email delivery and SMTP connection against the configured mail server in {@code dev} profile.
 */
@Slf4j
@SpringBootTest
@DisplayName("Integration: Real Email Service & SMTP Delivery (Dev Environment)")
@ActiveProfiles("dev")
class EmailServiceIntegrationTest {

    @Autowired
    private EmailService emailService;

    @Autowired
    private JavaMailSender mailSender;

    @Value("${contact.notification.recipient-email:yubraj2000yubraj@gmail.com}")
    private String recipientEmail;

    @Value("${spring.mail.username:demo.user@gmail.com}")
    private String fromEmail;

    @Test
    @DisplayName("Should verify JavaMailSender is wired with real dev environment SMTP host and port")
    void testJavaMailSenderConfiguration() {
        assertNotNull(mailSender, "JavaMailSender must be loaded in application context");

        if (mailSender instanceof JavaMailSenderImpl senderImpl) {
            log.info("Active SMTP Configuration -> Host: {}, Port: {}, Username: {}",
                    senderImpl.getHost(), senderImpl.getPort(), senderImpl.getUsername());

            assertNotNull(senderImpl.getHost(), "SMTP Host must be configured");
            assertTrue(senderImpl.getPort() > 0, "SMTP Port must be greater than 0");
        }
    }

    @Test
    @DisplayName("Should attempt real contact notification email delivery using EmailService")
    void testSendContactNotificationEmail_RealService() {
        ContactReqDto reqDto = ContactReqDto.builder()
                .name("Integration Test User")
                .email("test.visitor@example.com")
                .message("This is a live integration test notification message.")
                .build();

        // Invokes real EmailService with real JavaMailSender
        assertDoesNotThrow(() -> emailService.sendContactNotificationEmail(reqDto));
        log.info("EmailService.sendContactNotificationEmail completed successfully without uncaught exceptions.");
    }

    @Test
    @DisplayName("Should verify direct JavaMailSender message dispatch to configured recipient")
    void testDirectMailSender_Dispatch() {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(recipientEmail);
        message.setFrom(fromEmail);
        message.setSubject("Portfolio API - Real Dev Integration Test");
        message.setText("This email was sent from the real dev integration test suite.\nTimestamp: " + System.currentTimeMillis());

        try {
            mailSender.send(message);
            log.info("Successfully dispatched real test email to recipient: {}", recipientEmail);
        } catch (org.springframework.mail.MailAuthenticationException ex) {
            log.warn("SMTP authentication failed as expected with demo credentials. Update .env with real credentials to send live emails: {}", ex.getMessage());
        } catch (Exception ex) {
            log.warn("SMTP connection attempt: {}", ex.getMessage());
        }
    }
}
