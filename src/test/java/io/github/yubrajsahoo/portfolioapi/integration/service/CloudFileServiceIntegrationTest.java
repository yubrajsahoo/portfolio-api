package io.github.yubrajsahoo.portfolioapi.integration.service;

import io.github.yubrajsahoo.portfolioapi.dto.CloudFileDto;
import io.github.yubrajsahoo.portfolioapi.enums.AccessType;
import io.github.yubrajsahoo.portfolioapi.helper.DataBuilderUtils;
import io.github.yubrajsahoo.portfolioapi.service.CloudFileService;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockMultipartFile;

import java.io.InputStream;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertNotNull;

/**
 * Integration tests for {@link CloudFileService}.
 * <p>
 * This class tests the upload, retrieval, and deletion of files in Cloudinary
 * through the service layer without mocking any dependencies.
 */
@Slf4j
@SpringBootTest
@DisplayName("Integration: Cloud File Service Operations")
class CloudFileServiceIntegrationTest {

    @Autowired
    private CloudFileService cloudFileService;

    /**
     * Tests uploading a public file via CloudFileService.
     */
    @Test
    @DisplayName("Should Successfully Upload Public File via Service (Integration)")
    @Disabled("Due to storing data in Cloudinary")
    void testUpload_Public() throws Exception {
        InputStream inputStream = DataBuilderUtils.readFile("src/test/resources/images/logo.png");
        MockMultipartFile file = new MockMultipartFile("file", "logo.png", "image/png", inputStream);

        String url = cloudFileService.upload(file, AccessType.PUBLIC);
        log.info("Public File URL: {}", url);
        assertNotNull(url);
    }

    /**
     * Tests uploading a private file via CloudFileService.
     */
    @Test
    @DisplayName("Should Successfully Upload Private File via Service (Integration)")
    @Disabled("Due to storing data in Cloudinary")
    void testUpload_Private() throws Exception {
        InputStream inputStream = DataBuilderUtils.readFile("src/test/resources/pdf/Yubraj-Resume.pdf");
        MockMultipartFile file = new MockMultipartFile("file", "Yubraj-Resume.pdf", "application/pdf", inputStream);

        String url = cloudFileService.upload(file, AccessType.PRIVATE);
        log.info("Private File URL: {}", url);
        assertNotNull(url);
    }

    /**
     * Tests generating a download URL for a public file.
     */
    @Test
    @DisplayName("Should Generate URL for Public File via Service (Integration)")
    void testGetUrl_Public() {
        String url = cloudFileService.getUrl("logo.png", AccessType.PUBLIC);
        log.info("The Public URL is : {}", url);
        assertNotNull(url);
    }

    /**
     * Tests generating a download URL for a private file.
     */
    @Test
    @DisplayName("Should Generate URL for Private File via Service (Integration)")
    @Disabled("Due to hitting Cloudinary")
    void testGetUrl_Private() {
        String url = cloudFileService.getUrl("Yubraj-Resume.pdf", AccessType.PRIVATE);
        log.info("The Private URL is : {}", url);
        assertNotNull(url);
    }

    /**
     * Tests deleting a public file.
     */
    @Test
    @DisplayName("Should Delete Public File via Service (Integration)")
    @Disabled("Due to hitting Cloudinary")
    void testDelete_Public() {
        org.junit.jupiter.api.Assertions.assertDoesNotThrow(() -> cloudFileService.delete("logo.png", AccessType.PUBLIC));
    }

    /**
     * Tests deleting a private file.
     */
    @Test
    @DisplayName("Should Delete Private File via Service (Integration)")
    @Disabled("Due to hitting Cloudinary")
    void testDelete_Private() {
        org.junit.jupiter.api.Assertions.assertDoesNotThrow(() -> cloudFileService.delete("Yubraj-Resume.pdf", AccessType.PRIVATE));
    }

    /**
     * Tests fetching all public file names from Cloudinary.
     */
    @Test
    @DisplayName("Should Fetch All Public File Names via Service (Integration)")
    void testGetAllFileNames_Public() {
        List<CloudFileDto> files = cloudFileService.getAllFileNames(AccessType.PUBLIC);
        log.info("Total public files fetched: {}", files.size());
        files.forEach(f -> log.info("File name: {}", f.getFileName()));
        assertNotNull(files);
    }

    /**
     * Tests fetching all private file names from Cloudinary.
     */
    @Test
    @DisplayName("Should Fetch All Private File Names via Service (Integration)")
    void testGetAllFileNames_Private() {
        List<CloudFileDto> files = cloudFileService.getAllFileNames(AccessType.PRIVATE);
        log.info("Total private files fetched: {}", files.size());
        files.forEach(f -> log.info("Private File name: {}", f.getFileName()));
        assertNotNull(files);
    }
}
