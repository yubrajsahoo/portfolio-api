/*
 *
 *  * Copyright (c) 2026 Yubraj Sahoo. All rights reserved.
 *
 */

package io.github.yubrajsahoo.portfolioapi.entity;

import io.github.yubrajsahoo.portfolioapi.constants.LocationConstants;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Represents a user in the system.
 * Contains user credentials and associated roles.
 *
 * @author Yubraj Sahoo
 */
@Getter
@Setter
@Entity
@Table(name = "users")
public class User {

    /**
     * The unique identifier for the user.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * The user's email address, used as a unique username.
     */
    @Email(message = "Invalid Email Id")
    @Column(unique = true, nullable = false)
    private String email;

    /**
     * The user's hashed password.
     */
    @Column(nullable = false)
    private String password;

    /**
     * The roles assigned to the user.
     */
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "user_roles",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id")
    )
    private List<Role> roles;

    /**
     * The timestamp of when the user was last updated.
     */
    @Setter(AccessLevel.NONE)
    private LocalDateTime updatedAt;

    /**
     * The timestamp of when the user was created.
     */
    @Setter(AccessLevel.NONE)
    private LocalDateTime createdAt;

    /**
     * Callback method triggered before the entity is persisted for the first time.
     * Sets the creation and update timestamps.
     */
    @PrePersist
    public void prePersist() {
        this.createdAt = LocalDateTime.now(LocationConstants.SERVER_LOCATION);
        this.updatedAt = LocalDateTime.now(LocationConstants.SERVER_LOCATION);
    }

    /**
     * Callback method triggered before the entity is updated.
     * Sets the update timestamp.
     */
    @PreUpdate
    public void preUpdate() {
        this.updatedAt = LocalDateTime.now(LocationConstants.SERVER_LOCATION);
    }
}
