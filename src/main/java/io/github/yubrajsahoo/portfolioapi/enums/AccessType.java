/*
 *
 *  * Copyright (c) 2026 Yubraj Sahoo. All rights reserved.
 *
 */

package io.github.yubrajsahoo.portfolioapi.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;

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

    /**
     * Determines the {@link AccessType} based on its Cloudinary name representation.
     *
     * @param cloudinaryName the access type string returned by Cloudinary (e.g., "upload", "authenticated")
     * @return the corresponding {@link AccessType}, or {@link AccessType#PUBLIC} if no match is found
     */
    public static AccessType fromCloudinary(String cloudinaryName) {
        return Arrays.stream(AccessType.values())
                .filter(accessType -> accessType.cloudinary.equalsIgnoreCase(cloudinaryName))
                .findFirst()
                .orElse(PUBLIC);
    }
}