
/*
 *
 *  * Copyright (c) 2026 Yubraj Sahoo. All rights reserved.
 *
 */

package io.github.yubrajsahoo.portfolioapi.service.impl;

import io.github.yubrajsahoo.portfolioapi.dto.ContactMessageDto;
import io.github.yubrajsahoo.portfolioapi.dto.ContactReqDto;
import io.github.yubrajsahoo.portfolioapi.dto.ContactResDto;
import io.github.yubrajsahoo.portfolioapi.entity.ContactMessage;
import io.github.yubrajsahoo.portfolioapi.mapper.CustomMapper;
import io.github.yubrajsahoo.portfolioapi.metrics.MetricsDescriptions;
import io.github.yubrajsahoo.portfolioapi.metrics.MetricsKeys;
import io.github.yubrajsahoo.portfolioapi.metrics.MetricsNames;
import io.github.yubrajsahoo.portfolioapi.metrics.MetricsValues;
import io.github.yubrajsahoo.portfolioapi.repository.ContactMessageRepository;
import io.github.yubrajsahoo.portfolioapi.service.ContactService;
import io.github.yubrajsahoo.smf4j.api.annotation.Tags;
import io.github.yubrajsahoo.smf4j.api.annotation.Timer;
import io.github.yubrajsahoo.smf4j.api.constant.Smf4jSpelConstants;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Implementation of {@link ContactService} for persisting and managing contact submissions.
 *
 * @author Yubraj Sahoo
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class ContactServiceImpl implements ContactService {

    private final ContactMessageRepository contactMessageRepository;
    private final CustomMapper customMapper;
    private final io.github.yubrajsahoo.portfolioapi.service.EmailService emailService;

    /**
     * Saves and processes a contact message received from the portfolio.
     *
     * @param contactReqDto the contact form payload
     * @return a {@link ContactResDto} indicating success and confirmation message
     */
    @Override
    @Transactional
    @Timer(
            name = MetricsNames.SERVICE,
            description = MetricsDescriptions.SERVICE_METHOD,
            tags = {
                    @Tags(key = MetricsKeys.API, value = MetricsValues.CONTACT_SERVICE),
                    @Tags(key = MetricsKeys.OPERATION, value = MetricsValues.SEND_CONTACT_MESSAGE),
                    @Tags(key = MetricsKeys.METHOD, value = Smf4jSpelConstants.METHOD_NAME),
                    @Tags(key = MetricsKeys.OUTCOME, value = MetricsValues.GENERAL_OUTCOME)
            }
    )
    public ContactResDto submitContactMessage(ContactReqDto contactReqDto) {
        log.info("Received contact form submission from: {} ({})", contactReqDto.getName(), contactReqDto.getEmail());

        ContactMessage contactMessage = customMapper.toContactMessageEntity(contactReqDto);
        ContactMessage savedMessage = contactMessageRepository.save(contactMessage);

        log.info("Contact message saved successfully with id: {}", savedMessage.getId());

        emailService.sendContactNotificationEmail(contactReqDto);

        return ContactResDto.builder()
                .success(true)
                .message("Thank you for reaching out! Your message has been received.")
                .build();
    }

    /**
     * Retrieves all submitted contact messages.
     *
     * @return a list of all contact messages mapped to DTOs
     */
    @Override
    @Transactional(readOnly = true)
    @Timer(
            name = MetricsNames.SERVICE,
            description = MetricsDescriptions.SERVICE_METHOD,
            tags = {
                    @Tags(key = MetricsKeys.API, value = MetricsValues.CONTACT_SERVICE),
                    @Tags(key = MetricsKeys.OPERATION, value = MetricsValues.GET_ALL_CONTACT_MESSAGES),
                    @Tags(key = MetricsKeys.METHOD, value = Smf4jSpelConstants.METHOD_NAME),
                    @Tags(key = MetricsKeys.OUTCOME, value = MetricsValues.GENERAL_OUTCOME)
            }
    )
    public List<ContactMessageDto> getAllContactMessages() {
        return contactMessageRepository.findAllByOrderByCreatedAtDesc()
                .stream()
                .map(customMapper::toContactMessageDto)
                .toList();
    }
}
