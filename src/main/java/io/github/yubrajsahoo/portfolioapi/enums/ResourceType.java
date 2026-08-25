/*
 *
 *  * Copyright (c) 2026 Yubraj Sahoo. All rights reserved.
 *
 */

package io.github.yubrajsahoo.portfolioapi.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;
import java.util.Set;

/**
 * Represents the resource types supported by Cloudinary.
 *
 * @author Yubraj Sahoo
 * @since 0.0.1-SNAPSHOT
 */
@Getter
@AllArgsConstructor
public enum ResourceType {

    /**
     * Image resource (e.g., png, jpg, gif, webp, svg, heic, avif).
     */
    IMAGE("image", Set.of("png", "jpg", "gif", "webp", "svg", "pdf")),

    /**
     * Video and audio resource (e.g., mp4, mov, webm, mp3, wav, flac).
     */
    VIDEO("video", Set.of("mp4", "mov", "webm")),

    /**
     * Raw file resource (e.g.,  docx, zip, json, txt, binaries).
     */
    RAW("raw", Set.of("docx", "zip", "json", "txt")),

    /**
     * Automatic resource type detection by Cloudinary.
     */
    AUTO("auto", Set.of());

    /**
     * Cloudinary resource type value.
     */
    private final String cloudinary;
    private final Set<String> cloudinaryExtensions;

    /**
     * Determines the ResourceType from a file extension.
     *
     * @param extension file extension without dot
     * @return corresponding {@link ResourceType}
     */
    public static ResourceType fromExtension(String extension) {
        if (extension == null || extension.isBlank()) {
            return RAW;
        }
        String ext = extension.trim().toLowerCase();

        return Arrays.stream(ResourceType.values())
                .filter(resourceType -> resourceType.cloudinaryExtensions.contains(ext))
                .findFirst()
                .orElse(ResourceType.RAW);
    }
}