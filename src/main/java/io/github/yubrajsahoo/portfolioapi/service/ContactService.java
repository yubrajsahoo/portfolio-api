/*
 *
 *  * Copyright (c) 2026 Yubraj Sahoo. All rights reserved.
 *
 */

package io.github.yubrajsahoo.portfolioapi.service;

import io.github.yubrajsahoo.portfolioapi.dto.ContactMessageDto;
import io.github.yubrajsahoo.portfolioapi.dto.ContactReqDto;
import io.github.yubrajsahoo.portfolioapi.dto.ContactResDto;

import java.util.List;

/**
 * Service interface for handling contact messages.
 *
 * @author Yubraj Sahoo
 */
public interface ContactService {

    /**
     * Saves and processes a contact message received from the portfolio.
     *
     * @param contactReqDto the contact form payload
     * @return a {@link ContactResDto} indicating success and message
     */
    ContactResDto submitContactMessage(ContactReqDto contactReqDto);

    /**
     * Retrieves all contact messages for administrative inspection.
     *
     * @return a list of {@link ContactMessageDto}
     */
    List<ContactMessageDto> getAllContactMessages();
}
