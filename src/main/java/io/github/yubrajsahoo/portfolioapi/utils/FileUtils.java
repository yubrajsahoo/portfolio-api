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
     * Builds a public ID for a file to be uploaded to Cloudinary.
     * The public ID is a unique identifier for the file in Cloudinary.
     *
     * @param accessType   The access type for the file.
     * @param resourceType The type of the resource (e.g., image, video).
     * @param fileName     The name of the file.
     * @return The public ID for the file.
     */
    public static String buildPublicId(AccessType accessType, ResourceType resourceType, String fileName) {
        return String.format("%s/%s/%s/%s",
                PORTFOLIO_FOLDER,
                accessType.getCloudinary(),
                resourceType.getCloudinary(),
                removeFileExtension(fileName)
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
     * @param filePath raw file path or name
     * @return sanitized, relative path
     * @throws IllegalArgumentException if the file path is null, blank, or invalid
     */
    public static String sanitizeFileName(String filePath) {
        Assert.hasText(filePath, "File path must not be null or blank");

        String sanitized = filePath.trim()
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

        if (sanitized.isBlank()) {
            throw new IllegalArgumentException("Sanitized file path cannot be empty for input: " + filePath);
        }

        return sanitized;
    }
}
