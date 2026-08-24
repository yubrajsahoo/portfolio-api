package io.github.yubrajsahoo.portfolioapi.client.impl;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import io.github.yubrajsahoo.portfolioapi.client.CloudClient;
import io.github.yubrajsahoo.portfolioapi.config.CloudinaryProperties;
import io.github.yubrajsahoo.portfolioapi.enums.AccessType;
import io.github.yubrajsahoo.portfolioapi.enums.ResourceType;
import io.github.yubrajsahoo.portfolioapi.exception.CloudinaryException;
import io.github.yubrajsahoo.portfolioapi.metrics.MetricsType;
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
     * {@inheritDoc}
     *
     * <p>This implementation uploads the file to Cloudinary with the specified {@link AccessType}.
     * It sanitizes the file name, determines the correct resource type based on the file extension,
     * and sets the appropriate options for public or private access.
     */
    @Override
    public String upload(AccessType accessType, String fileName, InputStream inputStream) {
        Assert.notNull(accessType, "AccessType must not be null");
        validateArg(accessType, fileName);
        Assert.notNull(inputStream, "Unable to read file");

        fileName = FileUtils.sanitizeFileName(fileName);
        ResourceType resourceType = ResourceType.fromExtension(FileUtils.getFileExtension(fileName));
        String publicId = FileUtils.buildPublicId(accessType, resourceType, fileName);

        Map<?, ?> option = ObjectUtils.asMap(
                PUBLIC_ID, publicId,
                RESOURCE_TYPE, resourceType.getCloudinary(),
                TYPE, accessType.getCloudinary(),
                OVERWRITE, true
        );

        Map<?, ?> uploadResult;
        try {
            uploadResult = cloudinary.uploader()
                    .upload(inputStream.readAllBytes(), option);

        } catch (Exception exception) {
            throw new CloudinaryException("Exception occurred while uploading file to Cloudinary",
                    MetricsType.EXTERNAL_ERROR, exception);
        }

        if (uploadResult == null || uploadResult.get(SECURE_URL) == null) {
            throw new CloudinaryException("Error while uploading file with name : " + fileName);
        }
        return uploadResult.get(SECURE_URL).toString();
    }

    /**
     * {@inheritDoc}
     *
     * <p>For public files, generates a standard Cloudinary URL.
     * For private files, generates a secure signed download URL valid for the TTL
     * defined in the properties.
     */
    @Override
    public String getUrl(AccessType accessType, String fileName) {
        validateArg(accessType, fileName);

        String extension = FileUtils.getFileExtension(fileName);
        ResourceType resourceType = ResourceType.fromExtension(extension);
        String publicId = FileUtils.buildPublicId(accessType, resourceType, fileName);
        long expiresAt = (System.currentTimeMillis() + properties.privateUrlTtl().toMillis()) / 1000L;

        try {
            switch (accessType) {
                case PUBLIC -> {
                    return cloudinary.url()
                            .secure(true)
                            .resourceType(resourceType.getCloudinary())
                            .type(accessType.getCloudinary())
                            .format(extension)
                            .generate(publicId);
                }
                case PRIVATE -> {
                    Map<String, Object> option = Map.of(
                            RESOURCE_TYPE, resourceType.getCloudinary(),
                            TYPE, accessType.getCloudinary(),
                            EXPIRES_AT, expiresAt);
                    return cloudinary.privateDownload(publicId, extension, option);
                }
                default -> throw new CloudinaryException("Unsupported access type");
            }
        } catch (Exception exception) {
            throw new CloudinaryException("Failed to generate URL for file: " + fileName,
                    MetricsType.EXTERNAL_ERROR, exception);
        }
    }

    /**
     * {@inheritDoc}
     *
     * <p>Removes the file from Cloudinary storage using its generated public ID,
     * maintaining consistency with the assigned {@link AccessType} and {@link ResourceType}.
     */
    @Override
    public void delete(AccessType accessType, String fileName) {
        validateArg(accessType, fileName);

        String extension = FileUtils.getFileExtension(fileName);
        ResourceType resourceType = ResourceType.fromExtension(extension);
        String publicId = FileUtils.buildPublicId(accessType, resourceType, fileName);
        Map<String, String> option = Map.of(
                RESOURCE_TYPE, resourceType.getCloudinary(),
                TYPE, accessType.getCloudinary()
        );

        try {
            cloudinary.uploader()
                    .destroy(publicId, option);
        } catch (Exception exception) {
            throw new CloudinaryException("Exception occurred while deleting file from Cloudinary: " + fileName,
                    MetricsType.EXTERNAL_ERROR, exception);
        }
    }

    private void validateArg(AccessType accessType, String fileName) {
        Assert.notNull(accessType, "AccessType must not be null");
        Assert.hasText(fileName, "File name must not be null or blank");
    }
}
