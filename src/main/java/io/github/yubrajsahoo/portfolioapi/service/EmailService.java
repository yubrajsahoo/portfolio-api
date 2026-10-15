/*
 *
 *  * Copyright (c) 2026 Yubraj Sahoo. All rights reserved.
 *
 */

package io.github.yubrajsahoo.portfolioapi.service;

import io.github.yubrajsahoo.portfolioapi.dto.ContactReqDto;

/**
 * Service interface for sending email notifications.
 *
 * @author Yubraj Sahoo
 */
public interface EmailService {

    /**
     * Sends an email notification when a new contact message is received.
     *
     * @param contactReqDto the contact form payload
     */
    void sendContactNotificationEmail(ContactReqDto contactReqDto);
}
