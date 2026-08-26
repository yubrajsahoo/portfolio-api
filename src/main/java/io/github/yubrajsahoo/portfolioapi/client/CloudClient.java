/*
 *
 *  * Copyright (c) 2026 Yubraj Sahoo. All rights reserved.
 *
 */

package io.github.yubrajsahoo.portfolioapi.client;

import io.github.yubrajsahoo.portfolioapi.domain.FileMetaData;

import java.io.InputStream;

/**
 * Client abstraction for file storage operations.
 *
 * @author Yubraj Sahoo
 * @since 0.0.1-SNAPSHOT
 */
public interface CloudClient {

    /**
     * Uploads a file to the cloud storage provider.
     *
     * @param inputStream the input stream containing the file content to be uploaded
     * @param metaData    the metadata associated with the file, such as file name and access type
     * @return the secure URL of the uploaded file
     * @throws RuntimeException if an error occurs during upload
     */
    String upload(InputStream inputStream, FileMetaData metaData);

    /**
     * Retrieves the URL for a given file based on its metadata.
     *
     * @param metaData the metadata associated with the file
     * @return the URL to access or download the file
     * @throws RuntimeException if an error occurs while retrieving the URL
     */
    String getUrl(FileMetaData metaData);

    /**
     * Deletes a file from the cloud storage provider.
     *
     * @param metaData the metadata associated with the file to be deleted
     * @throws RuntimeException if an error occurs during deletion
     */
    void delete(FileMetaData metaData);
}
