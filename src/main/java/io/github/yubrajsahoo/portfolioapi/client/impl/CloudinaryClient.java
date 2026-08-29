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
import io.github.yubrajsahoo.portfolioapi.contants.CloudinaryConstants;
import io.github.yubrajsahoo.portfolioapi.domain.FileMetaData;
import io.github.yubrajsahoo.portfolioapi.enums.AccessType;
import io.github.yubrajsahoo.portfolioapi.exception.CloudinaryException;
import io.github.yubrajsahoo.portfolioapi.exception.FileUploadException;
import io.github.yubrajsahoo.portfolioapi.metrics.MetricsType;
import io.github.yubrajsahoo.portfolioapi.utils.FileUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

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

    @Value("${cloudinary.application.name}")
    private String application;


    /**
     * Uploads a file to Cloudinary.
     *
     * @param inputStream the input stream containing the file content to be uploaded
     * @param metaData    the metadata associated with the file
     * @return the secure URL of the uploaded file in Cloudinary
     * @throws CloudinaryException if an error occurs during the upload process
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

        } catch (IOException ioException) {
            log.info("Unable to Read file: {}", ioException.getMessage(), ioException);
            throw new FileUploadException("Unable to Read File",
                    MetricsType.BAD_REQUEST, ioException);
        } catch (Exception exception) {
            log.warn("Unable to upload file in Cloudinary: {}", exception.getMessage(), exception);
            throw new CloudinaryException("Exception occurred while uploading file to Cloudinary",
                    MetricsType.EXTERNAL_ERROR, exception);
        }

        if (uploadResult == null || uploadResult.get(SECURE_URL) == null) {
            log.warn("Getting Invalid Response from Cloudinary: {}", uploadResult);
            throw new CloudinaryException("Error while uploading file with name : " + publicId,
                    MetricsType.EXTERNAL_ERROR);
        }

        return uploadResult.get(SECURE_URL).toString();
    }

    /**
     * Retrieves the URL for a given file stored in Cloudinary.
     * Generates a signed URL for private access or a standard secure URL for public access.
     *
     * @param metaData the metadata associated with the file
     * @return the generated file URL
     * @throws FileUploadException if the access type is unsupported
     * @throws CloudinaryException if an error occurs while generating the URL
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
            log.error("Getting Invalid AccessType : {}", exception.getMessage(), exception);
            throw exception;
        } catch (Exception exception) {
            log.warn("Getting Error while getting url: {}", exception.getMessage(), exception);
            throw new CloudinaryException("Failed to generate URL for file: " + metaData.getFileName(),
                    MetricsType.EXTERNAL_ERROR, exception);
        }
    }

    /**
     * Deletes a file from Cloudinary.
     *
     * @param metaData the metadata associated with the file to be deleted
     * @throws CloudinaryException if an error occurs during the deletion process
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
            log.warn("Unable to delete file: {}", exception.getMessage(), exception);
            throw new CloudinaryException("Exception occurred while deleting file from Cloudinary: " + metaData.getFileName(),
                    MetricsType.EXTERNAL_ERROR, exception);
        }
    }


    /**
     * Retrieves all file URLs stored in the portfolio folder for a given access type.
     *
     * @param accessType the access type of the files to retrieve (e.g., PUBLIC, PRIVATE)
     * @return a list of file URLs
     * @throws CloudinaryException if an error occurs while fetching files from Cloudinary
     */
    public List<String> getAllUrls(AccessType accessType) {
        List<String> fileUrls = new ArrayList<>();

        Map<?, ?> result;
        try {
            result = cloudinary.api()
                    .resources(ObjectUtils.asMap(
                            TYPE, accessType.getCloudinary(),
                            PREFIX, application + "/",
                            MAX_RESULT, 500
                    ));
        } catch (Exception exception) {
            throw new CloudinaryException("Unable to fetch all files from Cloudinary",
                    MetricsType.EXTERNAL_ERROR, exception);
        }
        if (Objects.isNull(result) || !result.containsKey(RESOURCES)) {
            throw new CloudinaryException("Not getting files from Cloudinary",
                    MetricsType.EXTERNAL_ERROR);
        }

        @SuppressWarnings("unchecked") List<Map<?, ?>> resources = (List<Map<?, ?>>) result.get(RESOURCES);

        for (Map<?, ?> res : resources) {
            String url = null;
            if (res.get(SECURE_URL) != null) {
                url = res.get(SECURE_URL).toString();
            } else if (res.get("url") != null) {
                url = res.get("url").toString();
            }

            if (url != null) {
                fileUrls.add(url);
            }
        }
        return fileUrls;
    }

    /**
     * Helper method to build the Cloudinary public ID for a given file.
     *
     * @param metaData the metadata associated with the file
     * @return the constructed public ID
     */
    private String buildPublicId(FileMetaData metaData) {
        return String.format(
                CloudinaryConstants.PUBLIC_ID_FORMAT,
                FileUtils.buildUploadFolder(application, metaData), metaData.getFileName()
        );
    }
}
