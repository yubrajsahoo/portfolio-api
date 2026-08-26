/*
 *
 *  * Copyright (c) 2026 Yubraj Sahoo. All rights reserved.
 *
 */

package io.github.yubrajsahoo.portfolioapi.utils;

import io.github.yubrajsahoo.portfolioapi.contants.CloudinaryConstants;
import io.github.yubrajsahoo.portfolioapi.enums.AccessType;
import io.github.yubrajsahoo.portfolioapi.enums.ResourceType;
import lombok.experimental.UtilityClass;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;

import static io.github.yubrajsahoo.portfolioapi.contants.CloudinaryConstants.PORTFOLIO_FOLDER;

/**
 * Utility class for generic file and JSON operations.
 *
 * @author Yubraj Sahoo
 * @since 0.0.1-SNAPSHOT
 */
@UtilityClass
public class FileUtils {

    /**
     * Method to build upload folder for portfolio website
     *
     * @param accessType   The access type for the file.
     * @param resourceType The type of the resource (e.g., image, video).
     * @return The public ID for the file.
     */
    public static String buildUploadFolder(AccessType accessType, ResourceType resourceType) {
        return String.format("%s/%s/%s",
                PORTFOLIO_FOLDER,
                accessType.getCloudinary(),
                resourceType.getCloudinary()
        );
    }

    /**
     * Extracts the file extension without the leading dot.
     *
     * @param fileName file name or path
     * @return lowercase extension without dot, or empty string
     */
    public static String getFileExtension(String fileName) {
        if (StringUtils.hasText(fileName)) {
            int dotIdx = fileName.lastIndexOf('.');
            if (dotIdx != -1 && dotIdx < fileName.length() - 1) {
                return fileName.substring(dotIdx + 1).toLowerCase();
            }
        }
        return "";
    }

    /**
     * Strips the file extension from a file name.
     *
     * @param fileName file name or path
     * @return file name without extension
     */
    public static String removeFileExtension(String fileName) {
        if (StringUtils.hasText(fileName)) {
            int dotIdx = fileName.lastIndexOf('.');
            if (dotIdx > 0) {
                return fileName.substring(0, dotIdx);
            }
        }
        return fileName;
    }

    /**
     * Sanitizes and normalizes a file for Cloudinary storage.
     *
     * <p>Performs the following:
     * <ul>
     *   <li>Validates that the input is not null or blank.</li>
     *   <li>Replaces Windows backslashes ({@code \}) with standard forward slashes ({@code /}).</li>
     *   <li>Prevents directory traversal attacks (e.g., removes {@code ../}).</li>
     *   <li>Collapses multiple consecutive slashes into a single slash.</li>
     *   <li>Strips leading and trailing slashes.</li>
     *   <li>Removes redundant root folder prefix (e.g., {@code portfolio/}) to prevent duplicate nesting.</li>
     * </ul>
     *
     * @param fielName raw file path or name
     * @return sanitized, relative path
     * @throws IllegalArgumentException if the file path is null, blank, or invalid
     */
    public static String sanitizeFileName(String fielName) {
        Assert.hasText(
                fielName,
                "File path must not be null or blank"
        );

        String sanitized = fielName.trim()
                .replace('\\', '/')
                .replaceAll("/+", "/")
                .replaceAll("\\.{2,}/", "")
                .replaceAll("^/+", "")
                .replaceAll("/+$", "");

        // Avoid duplicate root folder prefix (e.g., "portfolio/avatar.png" -> "avatar.png")
        String rootPrefix = CloudinaryConstants.PORTFOLIO_FOLDER + "/";
        if (sanitized.startsWith(rootPrefix)) {
            sanitized = sanitized.substring(rootPrefix.length());
        }

        Assert.hasText(
                sanitized,
                "File path must not be null or blank" + fielName
        );

        return sanitized;
    }

    /**
     * Asserts that a given file name has a valid extension and name.
     *
     * @param fileName the name of the file to validate
     * @throws IllegalArgumentException if the file extension or file name is missing
     */
    public static void assertFileName(String fileName) {
        Assert.hasText(
                FileUtils.getFileExtension(fileName),
                "File extension must not be null or blank: " + fileName
        );

        Assert.hasText(
                FileUtils.removeFileExtension(fileName),
                "File name should not be empty: " + fileName
        );
    }
}
