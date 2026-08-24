package io.github.yubrajsahoo.portfolioapi.client.impl;

import com.cloudinary.Cloudinary;
import io.github.yubrajsahoo.portfolioapi.client.CloudClient;
import io.github.yubrajsahoo.portfolioapi.config.CloudinaryProperties;
import io.github.yubrajsahoo.portfolioapi.enums.AccessType;
import io.github.yubrajsahoo.portfolioapi.enums.ResourceType;
import io.github.yubrajsahoo.portfolioapi.exception.CloudinaryException;
import io.github.yubrajsahoo.portfolioapi.utils.CloudinaryUtils;
import io.github.yubrajsahoo.portfolioapi.utils.FileUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

import java.io.InputStream;
import java.util.Map;

import static io.github.yubrajsahoo.portfolioapi.contants.CloudinaryConstants.*;

/**
 * Production-ready Cloudinary implementation of {@link CloudClient}.
 *
 * <p>Architected to handle all file types (images, videos, audio, documents, archives, binaries)
 * with consistent public/private delivery controls, time-bounded private access URLs, and robust error handling.
 *
 * @author Yubraj Sahoo
 * @since 0.0.1-SNAPSHOT
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class CloudinaryClient implements CloudClient {
    private final Cloudinary cloudinary;
    private final CloudinaryProperties properties;


    /**
     * Uploads a public file.
     *
     * @param inputStream file content
     * @param fileName    file name
     * @return publicly accessible file URL
     */
    @Override
    public String uploadPublic(InputStream inputStream, String fileName) {
        return executeUpload(inputStream, fileName, AccessType.PUBLIC);
    }

    /**
     * Uploads a private file.
     *
     * @param inputStream file content
     * @param fileName    file name
     * @return private file URL
     */
    @Override
    public String uploadPrivate(InputStream inputStream, String fileName) {
        return executeUpload(inputStream, fileName, AccessType.PRIVATE);
    }

    /**
     * Generates a private URL for a given file ID.
     *
     * @param fileId file ID
     * @return private file URL
     */
    @Override
    public String generatePrivateUrl(String fileId) {
        Assert.hasText(fileId, "File ID must not be null or blank");

        try {
            String extension = FileUtils.getFileExtension(fileId);
            ResourceType resourceType = ResourceType.fromExtension(extension);
            String publicId = CloudinaryUtils.buildPublicId(AccessType.PRIVATE, resourceType, fileId);

            long expiresAt = (System.currentTimeMillis() + properties.privateUrlTtl().toMillis()) / 1000L;

            return cloudinary.privateDownload(publicId, extension, Map.of(
                    RESOURCE_TYPE, resourceType.getCloudinary(),
                    TYPE, AccessType.PRIVATE.getCloudinary(),
                    EXPIRES_AT, expiresAt
            ));
        } catch (CloudinaryException cloudinaryException) {
            throw cloudinaryException;
        } catch (Exception exception) {
            throw new CloudinaryException("Failed to generate private file URL from Cloudinary: " + fileId, exception);
        }
    }

    /**
     * Deletes a public file.
     *
     * @param fileId Cloudinary public ID or file name
     */
    @Override
    public void deletePublic(String fileId) {
        executeDelete(fileId, AccessType.PUBLIC);
    }

    /**
     * Deletes a private file.
     *
     * @param fileId file ID or file name
     */
    @Override
    public void deletePrivate(String fileId) {
        executeDelete(fileId, AccessType.PRIVATE);
    }

    private void executeDelete(String fileId, AccessType accessType) {
        Assert.hasText(fileId, "File ID must not be null or blank");

        try {
            String extension = FileUtils.getFileExtension(fileId);
            ResourceType resourceType = ResourceType.fromExtension(extension);
            String publicId = CloudinaryUtils.buildPublicId(accessType, resourceType, fileId);

            cloudinary.uploader().destroy(publicId, Map.of(
                    RESOURCE_TYPE, resourceType.getCloudinary(),
                    TYPE, accessType.getCloudinary()
            ));
        } catch (CloudinaryException cloudinaryException) {
            throw cloudinaryException;
        } catch (Exception exception) {
            throw new CloudinaryException("Exception occurred while deleting file from Cloudinary: " + fileId, exception);
        }
    }

    private String executeUpload(InputStream inputStream, String fileName, AccessType accessType) {
        Assert.notNull(inputStream, "Unable to read file");
        Assert.hasText(fileName, "File name must not be null or blank");

        try {
            fileName = FileUtils.sanitizeFileName(fileName);
            Map<?, ?> option = CloudinaryUtils.buildUploadOption(fileName, accessType);
            Map<?, ?> uploadResult = cloudinary.uploader()
                    .upload(inputStream.readAllBytes(), option);

            return CloudinaryUtils.extractSecureUrl(uploadResult, fileName);
        } catch (CloudinaryException cloudinaryException) {
            throw cloudinaryException;
        } catch (Exception exception) {
            throw new CloudinaryException("Exception occurred while uploading file to Cloudinary", exception);
        }
    }
}
