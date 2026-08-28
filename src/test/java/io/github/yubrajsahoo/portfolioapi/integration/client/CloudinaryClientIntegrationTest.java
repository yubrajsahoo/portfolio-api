/*
 *
 *  * Copyright (c) 2026 Yubraj Sahoo. All rights reserved.
 *
 */

package io.github.yubrajsahoo.portfolioapi.integration.client;

import io.github.yubrajsahoo.portfolioapi.client.impl.CloudinaryClient;
import io.github.yubrajsahoo.portfolioapi.domain.FileMetaData;
import io.github.yubrajsahoo.portfolioapi.enums.AccessType;
import io.github.yubrajsahoo.portfolioapi.exception.CloudinaryException;
import io.github.yubrajsahoo.portfolioapi.helper.DataBuilderUtils;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.InputStream;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

/**
 * Integration tests for {@link CloudinaryClient}.
 * <p>
 * This class tests the upload, retrieval, and deletion of files in Cloudinary.
 */
@Slf4j
@SpringBootTest
@DisplayName("Integration: Cloudinary Client Operations")
class CloudinaryClientIntegrationTest {

    @Autowired
    private CloudinaryClient cloudinaryClient;

    /**
     * Tests uploading a public file to Cloudinary.
     */
    @Test
    @DisplayName("Should Successfully Upload Public File to Cloudinary")
    @Disabled("Due to storing data in Cloudinary")
    void testUpload_Public() {
        InputStream inputStream = DataBuilderUtils.readFile("src/test/resources/images/logo.png");
        FileMetaData metaData = DataBuilderUtils.readFromJson(
                "src/test/resources/json/file-meta-data-public-png-logo.json",
                FileMetaData.class
        );

        String publicId = cloudinaryClient.upload(inputStream, metaData);
        log.info("Public File ID:{}", publicId);
        assertNotNull(publicId);
    }

    /**
     * Tests uploading an empty file to Cloudinary and expects a CloudinaryException.
     */
    @Test
    @DisplayName("Should Reject Empty Public File Upload")
    @Disabled("Due to storing data in Cloudinary")
    void testUpload_Public_Empty() {
        InputStream inputStream = DataBuilderUtils.readFile("src/test/resources/images/empty-file.png");
        FileMetaData metaData = DataBuilderUtils.readFromJson(
                "src/test/resources/json/file-meta-data-public-png-logo.json",
                FileMetaData.class
        );

        assertThrows(CloudinaryException.class, () -> cloudinaryClient.upload(inputStream, metaData));
    }

    /**
     * Tests uploading a private file to Cloudinary.
     */
    @Test
    @DisplayName("Should Successfully Upload Private File to Cloudinary")
    @Disabled("Due to storing data in Cloudinary")
    void testUpload_Private() {
        InputStream inputStream = DataBuilderUtils.readFile("src/test/resources/pdf/Yubraj-Resume.pdf");
        FileMetaData metaData = DataBuilderUtils.readFromJson(
                "src/test/resources/json/file-meta-data-private-pdf-resume.json",
                FileMetaData.class
        );

        String publicId = cloudinaryClient.upload(inputStream, metaData);
        log.info("Private File ID:{}", publicId);
        assertNotNull(publicId);
    }

    /**
     * Tests generating a download URL for a public file in Cloudinary.
     */
    @Test
    @DisplayName("Should Generate Valid Download URL for Public File")
    void testGetUrl_Public() {
        FileMetaData metaData = DataBuilderUtils.readFromJson(
                "src/test/resources/json/file-meta-data-public-png-logo.json",
                FileMetaData.class
        );

        String publicUrl = cloudinaryClient.getUrl(metaData);
        log.info("The Public Url is : {}", publicUrl);
        assertNotNull(publicUrl);
    }

    /**
     * Tests generating a download URL for a private file in Cloudinary.
     */
    @Test
    @DisplayName("Should Generate Valid Download URL for Private File")
    @Disabled("Due to hitting Cloudinary")
    void testGetUrl_Private() {
        FileMetaData metaData = DataBuilderUtils.readFromJson(
                "src/test/resources/json/file-meta-data-private-pdf-resume.json",
                FileMetaData.class
        );

        String privateUrl = cloudinaryClient.getUrl(metaData);
        log.info("The Private Url is : {}", privateUrl);
        assertNotNull(privateUrl);
    }

    /**
     * Tests deleting a public file from Cloudinary.
     */
    @Test
    @DisplayName("Should Delete Public File Successfully")
    @Disabled("Due to hitting Cloudinary")
    void testDelete_Public() {
        FileMetaData metaData = DataBuilderUtils.readFromJson(
                "src/test/resources/json/file-meta-data-public-png-logo.json",
                FileMetaData.class
        );

        org.junit.jupiter.api.Assertions.assertDoesNotThrow(() -> cloudinaryClient.delete(metaData));
    }

    /**
     * Tests deleting a private file from Cloudinary.
     */
    @Test
    @DisplayName("Should Delete Private File Successfully")
    @Disabled("Due to hitting Cloudinary")
    void testDelete_Private() {
        FileMetaData metaData = DataBuilderUtils.readFromJson(
                "src/test/resources/json/file-meta-data-private-pdf-resume.json",
                FileMetaData.class
        );

        org.junit.jupiter.api.Assertions.assertDoesNotThrow(() -> cloudinaryClient.delete(metaData));
    }

    /**
     * Tests fetching all file names from Cloudinary.
     */
    @Test
    @DisplayName("Should Retrieve All Public File Names from Cloudinary")
    void testGetAllFileNames() {
        java.util.List<String> fileNames = cloudinaryClient.getAllUrls(AccessType.PUBLIC);
        log.info("Total file names fetched: {}", fileNames.size());
        fileNames.forEach(name -> log.info("File name: {}", name));
        assertNotNull(fileNames);
    }

    /**
     * Tests fetching all file names from Private.
     */
    @Test
    @DisplayName("Should Retrieve All Private File Names from Cloudinary")
    void testGetAllFileNames_Private() {
        java.util.List<String> fileNames = cloudinaryClient.getAllUrls(AccessType.PRIVATE);
        log.info("Total private file names fetched: {}", fileNames.size());
        fileNames.forEach(name -> log.info("Private File name: {}", name));
        assertNotNull(fileNames);
    }
}
