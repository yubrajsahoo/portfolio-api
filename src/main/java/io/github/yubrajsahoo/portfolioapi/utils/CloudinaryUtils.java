package io.github.yubrajsahoo.portfolioapi.utils;

import com.cloudinary.utils.ObjectUtils;
import io.github.yubrajsahoo.portfolioapi.enums.AccessType;
import io.github.yubrajsahoo.portfolioapi.enums.ResourceType;
import io.github.yubrajsahoo.portfolioapi.exception.CloudinaryException;
import lombok.experimental.UtilityClass;

import java.util.Map;

import static io.github.yubrajsahoo.portfolioapi.contants.CloudinaryConstants.*;

/**
 * This is a utility class for Cloudinary, a cloud-based image and video management service.
 * It provides helper methods for building Cloudinary upload options and public IDs,
 * and for extracting the secure URL from an upload result.
 *
 * <p>This class uses the {@code @UtilityClass} annotation from Lombok to generate a private constructor,
 * make all methods static, and prevent instantiation.</p>
 *
 * @see com.cloudinary.Cloudinary
 * @see lombok.experimental.UtilityClass
 */
@UtilityClass
public class CloudinaryUtils {

    /**
     * Builds a map of upload options for a file to be uploaded to Cloudinary.
     *
     * @param fileName   The name of the file to be uploaded.
     * @param accessType The access type for the file (e.g., public, private).
     * @return A map of upload options.
     */
    public static Map<?, ?> buildUploadOption(String fileName, AccessType accessType) {
        ResourceType resourceType = ResourceType.fromExtension(FileUtils.getFileExtension(fileName));

        return ObjectUtils.asMap(
                PUBLIC_ID, buildPublicId(accessType, resourceType, fileName),
                RESOURCE_TYPE, resourceType.getCloudinary(),
                TYPE, accessType.getCloudinary(),
                OVERWRITE, true
        );
    }

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
        return String.format(
                "%s/%s/%s/%s",
                PORTFOLIO_FOLDER,
                accessType.getCloudinary(),
                resourceType.getCloudinary(),
                fileName
        );
    }


    /**
     * Extracts the secure URL from a Cloudinary upload result.
     *
     * @param uploadResult The result of the upload operation.
     * @param fileName     The name of the file that was uploaded.
     * @return The secure URL of the uploaded file.
     * @throws CloudinaryException If the upload result is null or does not contain a secure URL.
     */
    public static String extractSecureUrl(Map<?, ?> uploadResult, String fileName) {
        if (uploadResult == null || uploadResult.get(SECURE_URL) == null) {
            throw new CloudinaryException("Error while uploading file with name : " + fileName);
        }
        return uploadResult.get(SECURE_URL).toString();
    }
}