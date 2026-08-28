/*
 *
 *  * Copyright (c) 2026 Yubraj Sahoo. All rights reserved.
 *
 */

package io.github.yubrajsahoo.portfolioapi.service;

import io.github.yubrajsahoo.portfolioapi.dto.CloudFileDto;
import io.github.yubrajsahoo.portfolioapi.enums.AccessType;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

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
     * @param file       the file to be uploaded
     * @param accessType the access type for the file to be uploaded
     * @return the URL or identifier of the uploaded file
     * @throws io.github.yubrajsahoo.portfolioapi.exception.FileUploadException if an error occurs during file upload
     */
    String upload(MultipartFile file, AccessType accessType);

    /**
     * Retrieves the URL for a stored file.
     *
     * @param fileName   the name of the file
     * @param accessType the access type of the file
     * @return the URL to access the file
     */
    String getUrl(String fileName, AccessType accessType);

    /**
     * Deletes a file from the cloud storage.
     *
     * @param fileName   the name of the file
     * @param accessType the access type of the file to delete
     */
    void delete(String fileName, AccessType accessType);

    /**
     * Retrieves all file names for files uploaded for a specific access type.
     *
     * @param accessType the access type of the files
     * @return a list of file data
     */
    List<CloudFileDto> getAllFileNames(AccessType accessType);
}
