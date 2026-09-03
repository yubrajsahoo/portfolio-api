/*
 *
 *  * Copyright (c) 2026 Yubraj Sahoo. All rights reserved.
 *
 */

package io.github.yubrajsahoo.portfolioapi.entity;

import io.github.yubrajsahoo.portfolioapi.constants.LocationConstants;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@Table(name = "privileges")
public class Privilege {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String name;

    @Setter(AccessLevel.NONE)
    private LocalDateTime updatedAt;

    @Setter(AccessLevel.NONE)
    private LocalDateTime createdAt;

    @PrePersist
    public void prePersist() {
        this.createdAt = LocalDateTime.now(LocationConstants.SERVER_LOCATION);
        this.updatedAt = LocalDateTime.now(LocationConstants.SERVER_LOCATION);
    }

    @PreUpdate
    public void preUpdate() {
        this.updatedAt = LocalDateTime.now(LocationConstants.SERVER_LOCATION);
    }
}
