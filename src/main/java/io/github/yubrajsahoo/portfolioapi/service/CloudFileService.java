/*
 *
 *  * Copyright (c) 2026 Yubraj Sahoo. All rights reserved.
 *
 */

package io.github.yubrajsahoo.portfolioapi.service;

import io.github.yubrajsahoo.portfolioapi.dto.FileMetaDto;
import org.springframework.web.multipart.MultipartFile;

/**
 * Service interface for managing files in a cloud storage system.
 *
 * @author Yubraj Sahoo
 * @version 0.0.1-SNAPSHOT
 */
public interface CloudFileService {

    /**
     * Uploads a file to the cloud storage.
     *
     * @param file    the file to be uploaded
     * @param metaDto the metaDto for the file to be uploaded
     * @return the URL or identifier of the uploaded file
     */
    String upload(MultipartFile file, FileMetaDto metaDto);

    /**
     * Retrieves the URL for a stored file.
     *
     * @param metaDto the metaDto of the file
     * @return the URL to access the file
     */
    String getUrl(FileMetaDto metaDto);

    /**
     * Deletes a file from the cloud storage.
     *
     * @param metaDto the metaDto of the file to delete
     */
    void delete(FileMetaDto metaDto);
}
