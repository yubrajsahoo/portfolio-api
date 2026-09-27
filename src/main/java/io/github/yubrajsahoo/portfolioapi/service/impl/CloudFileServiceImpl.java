/*
 *
 *  * Copyright (c) 2026 Yubraj Sahoo. All rights reserved.
 *
 */

package io.github.yubrajsahoo.portfolioapi.service.impl;

import io.github.yubrajsahoo.portfolioapi.cache.constants.CacheExpressions;
import io.github.yubrajsahoo.portfolioapi.cache.constants.CacheNames;
import io.github.yubrajsahoo.portfolioapi.client.CloudClient;
import io.github.yubrajsahoo.portfolioapi.domain.FileMetaData;
import io.github.yubrajsahoo.portfolioapi.dto.CloudFileDto;
import io.github.yubrajsahoo.portfolioapi.enums.AccessType;
import io.github.yubrajsahoo.portfolioapi.exception.FileUploadException;
import io.github.yubrajsahoo.portfolioapi.mapper.CustomMapper;
import io.github.yubrajsahoo.portfolioapi.metrics.*;
import io.github.yubrajsahoo.portfolioapi.service.CloudFileService;
import io.github.yubrajsahoo.smf4j.api.annotation.Tags;
import io.github.yubrajsahoo.smf4j.api.annotation.Timer;
import io.github.yubrajsahoo.smf4j.api.constant.Smf4jSpelConstants;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Objects;

/**
 * Service for managing files in a cloud storage system.
 *
 * @author Yubraj Sahoo
 * @version 0.0.1-SNAPSHOT
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class CloudFileServiceImpl implements CloudFileService {
    private final CloudClient cloudClient;
    private final CustomMapper customMapper;

    /**
     * Uploads a file to the cloud storage.
     *
     * @param file       the file to be uploaded
     * @param accessType the access type for the file to be uploaded
     * @return the URL or identifier of the uploaded file
     * @throws FileUploadException if an error occurs during file upload
     */
    @Timer(
            name = MetricsNames.SERVICE,
            description = MetricsDescriptions.SERVICE_METHOD,
            tags = {
                    @Tags(key = MetricsKeys.API, value = MetricsValues.CLOUD_FILE_SERVICE),
                    @Tags(key = MetricsKeys.OPERATION, value = MetricsValues.UPLOAD),
                    @Tags(key = MetricsKeys.ACCESS_TYPE, value = MetricsValues.ACCESS_TYPE),
                    @Tags(key = MetricsKeys.METHOD, value = Smf4jSpelConstants.METHOD_NAME),
                    @Tags(key = MetricsKeys.OUTCOME, value = MetricsValues.GENERAL_OUTCOME)
            }
    )
    @Override
    public String upload(MultipartFile file, AccessType accessType) {
        FileMetaData metaData = customMapper.toFileMetaData(file.getOriginalFilename(), accessType);
        try {
            return cloudClient.upload(file.getInputStream(), metaData);
        } catch (IOException e) {
            log.info("Unable to read file: {}", e.getMessage(), e);
            throw new FileUploadException("Unable To Read File", MetricsType.BAD_REQUEST, e);
        }
    }


    /**
     * Retrieves the URL for a stored file.
     *
     * @param fileName   the name of the file
     * @param accessType the access type of the file
     * @return the URL to access the file
     */
    @Timer(
            name = MetricsNames.SERVICE,
            description = MetricsDescriptions.SERVICE_METHOD,
            tags = {
                    @Tags(key = MetricsKeys.API, value = MetricsValues.CLOUD_FILE_SERVICE),
                    @Tags(key = MetricsKeys.OPERATION, value = MetricsValues.GET_URL),
                    @Tags(key = MetricsKeys.ACCESS_TYPE, value = MetricsValues.ACCESS_TYPE),
                    @Tags(key = MetricsKeys.METHOD, value = Smf4jSpelConstants.METHOD_NAME),
                    @Tags(key = MetricsKeys.OUTCOME, value = MetricsValues.GENERAL_OUTCOME)
            }
    )
    @Override
    @Cacheable(
            cacheNames = CacheNames.CLOUD_FILE_URL,
            key = CacheExpressions.CLOUD_GET_URL
    )
    public String getUrl(String fileName, AccessType accessType) {
        log.info("Fetching file url from service: {}", fileName);
        FileMetaData fileMetaData = customMapper.toFileMetaData(fileName, accessType);
        return cloudClient.getUrl(fileMetaData);
    }

    /**
     * Deletes a file from the cloud storage.
     *
     * @param fileName   the name of the file
     * @param accessType the access type of the file to delete
     */
    @Timer(
            name = MetricsNames.SERVICE,
            description = MetricsDescriptions.SERVICE_METHOD,
            tags = {
                    @Tags(key = MetricsKeys.API, value = MetricsValues.CLOUD_FILE_SERVICE),
                    @Tags(key = MetricsKeys.OPERATION, value = MetricsValues.DELETE),
                    @Tags(key = MetricsKeys.ACCESS_TYPE, value = MetricsValues.ACCESS_TYPE),
                    @Tags(key = MetricsKeys.METHOD, value = Smf4jSpelConstants.METHOD_NAME),
                    @Tags(key = MetricsKeys.OUTCOME, value = MetricsValues.GENERAL_OUTCOME)
            }
    )
    @Override
    public void delete(String fileName, AccessType accessType) {
        FileMetaData fileMetaData = customMapper.toFileMetaData(fileName, accessType);
        cloudClient.delete(fileMetaData);
    }

    /**
     * Retrieves all file names for files uploaded for a specific access type.
     *
     * @param accessType the access type of the files
     * @return a list of file data
     */
    @Timer(
            name = MetricsNames.SERVICE,
            description = MetricsDescriptions.SERVICE_METHOD,
            tags = {
                    @Tags(key = MetricsKeys.API, value = MetricsValues.CLOUD_FILE_SERVICE),
                    @Tags(key = MetricsKeys.OPERATION, value = MetricsValues.GET_ALL_URL),
                    @Tags(key = MetricsKeys.ACCESS_TYPE, value = MetricsValues.ACCESS_TYPE),
                    @Tags(key = MetricsKeys.METHOD, value = Smf4jSpelConstants.METHOD_NAME),
                    @Tags(key = MetricsKeys.OUTCOME, value = MetricsValues.GENERAL_OUTCOME)
            }
    )
    @Override
    @Cacheable(
            cacheNames = CacheNames.ALL_CLOUD_FILES,
            key = CacheExpressions.ALL_CLOUD_FILES
    )
    public java.util.List<CloudFileDto> getAllFileNames(AccessType accessType) {
        log.info("Fetching all file names from service: {}", accessType);
        return cloudClient.getAllUrls(accessType).stream()
                .map(customMapper::toCloudFileDto)
                .filter(cloudFileDto -> !Objects.isNull(cloudFileDto))
                .toList();
    }
}