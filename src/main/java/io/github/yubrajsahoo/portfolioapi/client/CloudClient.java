package io.github.yubrajsahoo.portfolioapi.client;

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
     * @param accessType access type (PUBLIC or PRIVATE)
     * @param fileName file name with extension
     * @param inputStream file content
     * @return file URL
     */
    String upload(io.github.yubrajsahoo.portfolioapi.enums.AccessType accessType, String fileName, InputStream inputStream);

    /**
     * Retrieves the URL for a given file.
     *
     * @param accessType access type (PUBLIC or PRIVATE)
     * @param fileName file name with extension
     * @return file URL
     */
    String getUrl(io.github.yubrajsahoo.portfolioapi.enums.AccessType accessType, String fileName);

    /**
     * Deletes a file.
     *
     * @param accessType access type (PUBLIC or PRIVATE)
     * @param fileName file name with extension
     */
    void delete(io.github.yubrajsahoo.portfolioapi.enums.AccessType accessType, String fileName);
}
