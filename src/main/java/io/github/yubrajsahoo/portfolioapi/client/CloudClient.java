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
     * Uploads a public file.
     *
     * @param inputStream file content
     * @param fileName    file name
     * @return publicly accessible file URL
     */
    String uploadPublic(InputStream inputStream, String fileName);

    /**
     * Uploads a private file.
     *
     * @param inputStream file content
     * @param fileName    file name
     * @return private file URL
     */
    String uploadPrivate(InputStream inputStream, String fileName);

    /**
     * Generates a private URL for a given file ID.
     *
     * @param fileId file ID
     * @return private file URL
     */
    String generatePrivateUrl(String fileId);

    /**
     * Deletes a public file.
     *
     * @param fileId Cloudinary public ID or file name
     */
    void deletePublic(String fileId);

    /**
     * Deletes a private file.
     *
     * @param fileId file ID or file name
     */
    void deletePrivate(String fileId);
}
