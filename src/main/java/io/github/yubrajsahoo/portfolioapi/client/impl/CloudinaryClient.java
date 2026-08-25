/*
 *
 *  * Copyright (c) 2026 Yubraj Sahoo. All rights reserved.
 *
 */

package io.github.yubrajsahoo.portfolioapi.client.impl;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import io.github.yubrajsahoo.portfolioapi.client.CloudClient;
import io.github.yubrajsahoo.portfolioapi.config.CloudinaryProperties;
import io.github.yubrajsahoo.portfolioapi.domain.FileMetaData;
import io.github.yubrajsahoo.portfolioapi.exception.CloudinaryException;
import io.github.yubrajsahoo.portfolioapi.exception.FileUploadException;
import io.github.yubrajsahoo.portfolioapi.metrics.MetricsType;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

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
     * Uploads a file.
     *
     * @param inputStream file content
     * @param metaData    the file metadata
     * @return file URL
     */
    @Override
    public String upload(InputStream inputStream, FileMetaData metaData) {
        String publicId = buildPublicId(metaData);

        Map<?, ?> option = ObjectUtils.asMap(
                PUBLIC_ID, publicId,
                RESOURCE_TYPE, metaData.getResourceType().getCloudinary(),
                TYPE, metaData.getAccessType().getCloudinary(),
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
            throw new CloudinaryException("Error while uploading file with name : " + publicId,
                    MetricsType.EXTERNAL_ERROR);
        }

        return uploadResult.get(SECURE_URL).toString();
    }

    /**
     * Retrieves the URL for a given file.
     *
     * @param metaData the file metadata
     * @return file URL
     */
    @Override
    public String getUrl(FileMetaData metaData) {
        String publicId = buildPublicId(metaData);
        long expiresAt = (System.currentTimeMillis() + properties.privateUrlTtl().toMillis()) / 1000L;

        try {
            switch (metaData.getAccessType()) {
                case PUBLIC -> {
                    return cloudinary.url()
                            .secure(true)
                            .resourceType(metaData.getResourceType().getCloudinary())
                            .type(metaData.getAccessType().getCloudinary())
                            .format(metaData.getExtension())
                            .generate(publicId);
                }
                case PRIVATE -> {
                    Map<String, Object> option = Map.of(
                            RESOURCE_TYPE, metaData.getResourceType().getCloudinary(),
                            TYPE, metaData.getAccessType().getCloudinary(),
                            EXPIRES_AT, expiresAt);
                    return cloudinary.privateDownload(publicId, metaData.getExtension(), option);
                }
                default -> throw new FileUploadException("Unsupported access type", MetricsType.ERROR);
            }
        } catch (FileUploadException exception) {
            throw exception;
        } catch (Exception exception) {
            throw new CloudinaryException("Failed to generate URL for file: " + metaData.getFileName(),
                    MetricsType.EXTERNAL_ERROR, exception);
        }
    }

    /**
     * Deletes a file.
     *
     * @param metaData the file metadata
     */
    @Override
    public void delete(FileMetaData metaData) {
        String publicId = buildPublicId(metaData);
        Map<String, String> option = Map.of(
                RESOURCE_TYPE, metaData.getResourceType().getCloudinary(),
                TYPE, metaData.getAccessType().getCloudinary()
        );

        try {
            cloudinary.uploader()
                    .destroy(publicId, option);
        } catch (Exception exception) {
            throw new CloudinaryException("Exception occurred while deleting file from Cloudinary: " + metaData.getFileName(),
                    MetricsType.EXTERNAL_ERROR, exception);
        }
    }

    /**
     * Method to build public id for cloudinary.
     *
     * @param metaData the file metadata.
     * @return the public id
     */
    private String buildPublicId(FileMetaData metaData) {
        return String.format(
                "%s/%s",
                metaData.getFolder(), metaData.getFileName()
        );
    }
}
