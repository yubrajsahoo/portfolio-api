/*
 *
 *  * Copyright (c) 2026 Yubraj Sahoo. All rights reserved.
 *
 */

package io.github.yubrajsahoo.portfolioapi.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * Data Transfer Object representing a saved contact message for administrative retrieval.
 *
 * @author Yubraj Sahoo
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ContactMessageDto {

    /**
     * Unique identifier of the message.
     */
    private Long id;

    /**
     * Sender's name.
     */
    private String name;

    /**
     * Sender's email.
     */
    private String email;

    /**
     * Message body.
     */
    private String message;

    /**
     * Whether the message has been marked as read.
     */
    private boolean isRead;

    /**
     * The timestamp when the message was received.
     */
    private LocalDateTime createdAt;
}
