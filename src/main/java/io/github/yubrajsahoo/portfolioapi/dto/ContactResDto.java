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

/**
 * Response Data Transfer Object for contact form submission.
 *
 * @author Yubraj Sahoo
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ContactResDto {

    /**
     * Indicates whether the submission was successful.
     */
    private boolean success;

    /**
     * A user-facing message describing the outcome.
     */
    private String message;
}
