/*
 *
 *  * Copyright (c) 2026 Yubraj Sahoo. All rights reserved.
 *
 */

package io.github.yubrajsahoo.portfolioapi.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Request Data Transfer Object for contact form submission.
 *
 * @author Yubraj Sahoo
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ContactReqDto {

    /**
     * The name of the sender.
     */
    @NotBlank(message = "Name cannot be blank")
    @Size(max = 100, message = "Name cannot exceed 100 characters")
    private String name;

    /**
     * The email address of the sender.
     */
    @NotBlank(message = "Email cannot be blank")
    @Email(message = "Invalid Email Id")
    private String email;

    /**
     * The content of the message.
     */
    @NotBlank(message = "Message cannot be blank")
    @Size(max = 5000, message = "Message cannot exceed 5000 characters")
    private String message;
}
