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
     * Uploads a file.
     *
     * @param inputStream file content
     * @param metaData    the file metadata
     * @return file URL
     */
    String upload(InputStream inputStream, FileMetaData metaData);

    /**
     * Retrieves the URL for a given file.
     *
     * @param metaData the file metadata
     * @return file URL
     */
    String getUrl(FileMetaData metaData);

    /**
     * Deletes a file.
     *
     * @param metaData the file metadata
     */
    void delete(FileMetaData metaData);
}
