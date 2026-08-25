/*
 *
 *  * Copyright (c) 2026 Yubraj Sahoo. All rights reserved.
 *
 */

package io.github.yubrajsahoo.portfolioapi.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * Represents Cloudinary delivery types.
 *
 * @author Yubraj Sahoo
 */
@Getter
@AllArgsConstructor
public enum AccessType {

    /**
     * Publicly accessible file.
     */
    PUBLIC("upload"),

    /**
     * Authenticated file requiring a signed URL.
     */
    PRIVATE("authenticated");

    /**
     * Cloudinary access type value
     */
    private final String cloudinary;
}