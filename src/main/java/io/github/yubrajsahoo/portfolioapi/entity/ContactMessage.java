/*
 *
 *  * Copyright (c) 2026 Yubraj Sahoo. All rights reserved.
 *
 */

package io.github.yubrajsahoo.portfolioapi.entity;

import io.github.yubrajsahoo.portfolioapi.constants.LocationConstants;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

import java.time.LocalDateTime;

/**
 * Represents a contact message submitted from the portfolio website.
 *
 * @author Yubraj Sahoo
 */
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "contact_messages")
public class ContactMessage {

    /**
     * The unique identifier for the contact message.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * The sender's name.
     */
    @NotBlank(message = "Name cannot be blank")
    @Column(nullable = false, length = 100)
    private String name;

    /**
     * The sender's email address.
     */
    @Email(message = "Invalid Email Id")
    @NotBlank(message = "Email cannot be blank")
    @Column(nullable = false)
    private String email;

    /**
     * The message content.
     */
    @NotBlank(message = "Message cannot be blank")
    @Column(nullable = false, columnDefinition = "TEXT")
    private String message;

    /**
     * Flag indicating whether the message has been read by the admin.
     */
    @Builder.Default
    @Column(nullable = false)
    private boolean isRead = false;

    /**
     * The timestamp of when the contact message was submitted.
     */
    @Setter(AccessLevel.NONE)
    private LocalDateTime createdAt;

    /**
     * Callback method triggered before the entity is persisted for the first time.
     * Sets the creation timestamp using the configured server location timezone.
     */
    @PrePersist
    public void prePersist() {
        this.createdAt = LocalDateTime.now(LocationConstants.SERVER_LOCATION);
    }
}
