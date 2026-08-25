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
import io.github.yubrajsahoo.portfolioapi.dto.FileMetaDto;
import io.github.yubrajsahoo.portfolioapi.exception.FileUploadException;
import io.github.yubrajsahoo.portfolioapi.mapper.CustomMapper;
import io.github.yubrajsahoo.portfolioapi.metrics.MetricsType;
import io.github.yubrajsahoo.portfolioapi.service.CloudFileService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

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
     * @param file    the file to be uploaded
     * @param metaDto the metadata for the file to be uploaded
     * @return the URL or identifier of the uploaded file
     */
    @Override
    public String upload(MultipartFile file, FileMetaDto metaDto) {
        FileMetaData metaData = customMapper.toFileMetaData(metaDto);
        try {
            return cloudClient.upload(file.getInputStream(), metaData);
        } catch (IOException e) {
            throw new FileUploadException("Unable To Read File", MetricsType.BAD_REQUEST, e);
        }
    }


    /**
     * Retrieves the URL for a stored file.
     *
     * @param metaDto the metaDto of the file
     * @return the URL to access the file
     */
    @Override
    @Cacheable(
            cacheNames = CacheNames.CLOUD_FILE_URL,
            key = CacheExpressions.CLOUD_GET_URL
    )
    public String getUrl(FileMetaDto metaDto) {
        FileMetaData fileMetaData = customMapper.toFileMetaData(metaDto);
        return cloudClient.getUrl(fileMetaData);
    }

    /**
     * Deletes a file from the cloud storage.
     *
     * @param metaDto the metaDto of the file to delete
     */
    @Override
    public void delete(FileMetaDto metaDto) {
        FileMetaData fileMetaData = customMapper.toFileMetaData(metaDto);
        cloudClient.delete(fileMetaData);
    }
}