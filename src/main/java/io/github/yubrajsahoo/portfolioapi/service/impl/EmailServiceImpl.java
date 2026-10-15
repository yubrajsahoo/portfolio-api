/*
 *
 *  * Copyright (c) 2026 Yubraj Sahoo. All rights reserved.
 *
 */

package io.github.yubrajsahoo.portfolioapi.service.impl;

import io.github.yubrajsahoo.portfolioapi.dto.ContactReqDto;
import io.github.yubrajsahoo.portfolioapi.service.EmailService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

/**
 * Implementation of {@link EmailService} using {@link JavaMailSender}.
 *
 * @author Yubraj Sahoo
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class EmailServiceImpl implements EmailService {

    private final JavaMailSender mailSender;

    @Value("${contact.notification.recipient-email:yubraj2000yubraj@gmail.com}")
    private String recipientEmail;

    @Value("${spring.mail.username:noreply@portfolio.com}")
    private String fromEmail;

    /**
     * Sends an email notification when a new contact message is received.
     *
     * @param contactReqDto the contact form payload
     */
    @Override
    public void sendContactNotificationEmail(ContactReqDto contactReqDto) {
        try {
            log.info("Sending contact notification email to: {} for sender: {}", recipientEmail, contactReqDto.getEmail());

            SimpleMailMessage mailMessage = new SimpleMailMessage();
            if (fromEmail != null && !fromEmail.isBlank()) {
                mailMessage.setFrom(fromEmail);
            }
            mailMessage.setTo(recipientEmail);
            mailMessage.setSubject("New Contact Message from " + contactReqDto.getName());
            mailMessage.setText(
                    "You have received a new contact message from your portfolio website.\n\n" +
                    "Name: " + contactReqDto.getName() + "\n" +
                    "Email: " + contactReqDto.getEmail() + "\n\n" +
                    "Message:\n" + contactReqDto.getMessage() + "\n\n" +
                    "--\nPortfolio Notification System"
            );

            mailSender.send(mailMessage);
            log.info("Contact notification email sent successfully to: {}", recipientEmail);
        } catch (Exception ex) {
            log.error("Failed to send contact notification email: {}", ex.getMessage(), ex);
        }
    }
}
