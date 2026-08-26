/*
 *
 *  * Copyright (c) 2026 Yubraj Sahoo. All rights reserved.
 *
 */

package io.github.yubrajsahoo.portfolioapi.unit.client;

import io.github.yubrajsahoo.portfolioapi.domain.FileMetaData;

import com.cloudinary.Cloudinary;
import com.cloudinary.Uploader;
import io.github.yubrajsahoo.portfolioapi.client.impl.CloudinaryClient;
import io.github.yubrajsahoo.portfolioapi.exception.CloudinaryException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

/**
 * Unit tests for {@link CloudinaryClient} using Spring Boot context and mock beans.
 */
@SpringBootTest(
        properties = {
                "cloudinary.cloud-name=test-cloud",
                "cloudinary.api-key=test-key",
                "cloudinary.api-secret=test-secret"
        }
)
@DisplayName("CloudinaryClient Unit Test Suite")
class CloudinaryClientUnitTest {

    @MockitoBean
    private Cloudinary cloudinary;

    @Autowired
    private CloudinaryClient cloudinaryClient;

    @Test
    @DisplayName("upload - valid parameters - successfully uploads and returns secure URL")
    void upload_validParams_returnsSecureUrl() throws Exception {
        // Arrange
        InputStream inputStream = new ByteArrayInputStream("test content".getBytes());

        Uploader mockUploader = mock(Uploader.class);
        when(cloudinary.uploader()).thenReturn(mockUploader);

        Map<String, String> uploadResult = new HashMap<>();
        uploadResult.put("secure_url", "https://res.cloudinary.com/test-cloud/image/upload/v1/portfolio/public/image/logo.png");

        when(mockUploader.upload(any(byte[].class), any(Map.class))).thenReturn(uploadResult);

        FileMetaData metaData = io.github.yubrajsahoo.portfolioapi.helper.DataBuilderUtils.readFromJson(
                "src/test/resources/json/file-meta-data-public-png-logo.json",
                FileMetaData.class
        );

        // Act
        String resultUrl = cloudinaryClient.upload(inputStream, metaData);

        // Assert
        assertNotNull(resultUrl, "The returned secure URL should not be null");
        assertEquals("https://res.cloudinary.com/test-cloud/image/upload/v1/portfolio/public/image/logo.png", resultUrl);
        verify(cloudinary, times(1)).uploader();
        verify(mockUploader, times(1)).upload(any(byte[].class), any(Map.class));
    }

    @Test
    @DisplayName("upload - null input stream - throws CloudinaryException")
    void upload_nullInputStream_throwsCloudinaryException() {
        // Arrange
        String fileName = "test-file.png";

        // Act & Assert
        CloudinaryException exception = assertThrows(CloudinaryException.class, () ->
                cloudinaryClient.upload(null
                        , FileMetaData.builder().fileName(fileName).accessType(io.github.yubrajsahoo.portfolioapi.enums.AccessType.PUBLIC).resourceType(io.github.yubrajsahoo.portfolioapi.enums.ResourceType.IMAGE).folder("portfolio/upload/image").extension("png").build())
        );
        assertEquals("Exception occurred while uploading file to Cloudinary", exception.getMessage());
    }

    @Test
    @DisplayName("upload - null file name - throws CloudinaryException")
    void upload_nullFileName_throwsCloudinaryException() {
        // Arrange
        InputStream inputStream = new ByteArrayInputStream("test content".getBytes());

        // Act & Assert
        CloudinaryException exception = assertThrows(CloudinaryException.class, () ->
                cloudinaryClient.upload(inputStream
                        , FileMetaData.builder().fileName(null).accessType(io.github.yubrajsahoo.portfolioapi.enums.AccessType.PUBLIC).resourceType(io.github.yubrajsahoo.portfolioapi.enums.ResourceType.IMAGE).folder("portfolio/upload/image").extension("png").build())
        );
        assertEquals("Exception occurred while uploading file to Cloudinary", exception.getMessage());
    }

    @Test
    @DisplayName("upload - empty file name - throws CloudinaryException")
    void upload_emptyFileName_throwsCloudinaryException() {
        // Arrange
        InputStream inputStream = new ByteArrayInputStream("test content".getBytes());

        // Act & Assert
        CloudinaryException exception = assertThrows(
                CloudinaryException.class, () ->
                        cloudinaryClient.upload(inputStream
                                , FileMetaData.builder().fileName(" ").accessType(io.github.yubrajsahoo.portfolioapi.enums.AccessType.PUBLIC).resourceType(io.github.yubrajsahoo.portfolioapi.enums.ResourceType.IMAGE).folder("portfolio/upload/image").extension("png").build())
        );
        assertEquals("Exception occurred while uploading file to Cloudinary", exception.getMessage());
    }

    @Test
    @DisplayName("upload - CloudinaryException from uploader - propagates the CloudinaryException")
    void upload_cloudinaryException_propagatesException() throws Exception {
        // Arrange
        InputStream inputStream = new ByteArrayInputStream("test content".getBytes());
        String fileName = "test-file.png";

        Uploader mockUploader = mock(Uploader.class);
        when(cloudinary.uploader()).thenReturn(mockUploader);

        CloudinaryException cloudinaryException = new CloudinaryException("Cloudinary upload failed");
        when(mockUploader.upload(any(byte[].class), any(Map.class))).thenThrow(cloudinaryException);

        // Act & Assert
        CloudinaryException exception = assertThrows(
                CloudinaryException.class, () ->
                        cloudinaryClient.upload(inputStream
                                , FileMetaData.builder().fileName(fileName).accessType(io.github.yubrajsahoo.portfolioapi.enums.AccessType.PUBLIC).resourceType(io.github.yubrajsahoo.portfolioapi.enums.ResourceType.IMAGE).folder("portfolio/upload/image").extension("png").build())
        );
        assertEquals("Exception occurred while uploading file to Cloudinary", exception.getMessage());
    }

    @Test
    @DisplayName("upload - generic Exception from uploader - wraps and throws CloudinaryException")
    void upload_genericException_throwsCloudinaryException() throws Exception {
        // Arrange
        InputStream inputStream = new ByteArrayInputStream("test content".getBytes());
        String fileName = "test-file.png";

        Uploader mockUploader = mock(Uploader.class);
        when(cloudinary.uploader()).thenReturn(mockUploader);

        RuntimeException genericException = new RuntimeException("Generic upload failure");
        when(mockUploader.upload(any(byte[].class), any(Map.class))).thenThrow(genericException);

        // Act & Assert
        CloudinaryException exception = assertThrows(
                CloudinaryException.class, () ->
                        cloudinaryClient.upload(inputStream
                                , FileMetaData.builder().fileName(fileName).accessType(io.github.yubrajsahoo.portfolioapi.enums.AccessType.PUBLIC).resourceType(io.github.yubrajsahoo.portfolioapi.enums.ResourceType.IMAGE).folder("portfolio/upload/image").extension("png").build())
        );
        assertEquals("Exception occurred while uploading file to Cloudinary", exception.getMessage());
        assertEquals(genericException, exception.getCause());
    }

    @Test
    @DisplayName("getUrl - valid parameters - successfully generates signed private URL")
    void getUrl_validParams_returnsSignedUrl() throws Exception {
        // Arrange
        String fileId = "test-file";
        String expectedUrl = "https://res.cloudinary.com/test-cloud/image/authenticated/v1/portfolio/authenticated/image/test-file.png?s=signature";

        when(cloudinary.privateDownload(anyString(), anyString(), any(Map.class))).thenReturn(expectedUrl);

        // Act
        String resultUrl = cloudinaryClient.getUrl(FileMetaData.builder().fileName(fileId
        ).accessType(io.github.yubrajsahoo.portfolioapi.enums.AccessType.PRIVATE).resourceType(io.github.yubrajsahoo.portfolioapi.enums.ResourceType.IMAGE).folder("portfolio/upload/image").extension("png").build());

        // Assert
        assertNotNull(resultUrl);
        assertEquals(expectedUrl, resultUrl);
        verify(cloudinary, times(1)).privateDownload(eq("portfolio/upload/image/test-file"), eq("png"), any(Map.class));
    }

    @Test
    @DisplayName("getUrl - public parameters - successfully generates public secure URL")
    void getUrl_publicParams_returnsSecureUrl() {
        // Arrange
        String fileId = "test-file";

        // Cloudinary url() uses a builder pattern, we need to mock it carefully if it's deeply chained, 
        // OR rely on the real Cloudinary object since it's just generating a string offline.
        // But since 'cloudinary' is a @MockitoBean, we MUST mock the builder chain:
        com.cloudinary.Url mockUrl = mock(com.cloudinary.Url.class);
        when(cloudinary.url()).thenReturn(mockUrl);
        when(mockUrl.secure(true)).thenReturn(mockUrl);
        when(mockUrl.resourceType(anyString())).thenReturn(mockUrl);
        when(mockUrl.type(anyString())).thenReturn(mockUrl);
        when(mockUrl.format(anyString())).thenReturn(mockUrl);
        when(mockUrl.generate(anyString())).thenReturn(
                "https://res.cloudinary.com/test-cloud/image/upload/portfolio/upload/image/test-file.png"
        );

        // Act
        String resultUrl = cloudinaryClient.getUrl(FileMetaData.builder().fileName(fileId
        ).accessType(io.github.yubrajsahoo.portfolioapi.enums.AccessType.PUBLIC).resourceType(io.github.yubrajsahoo.portfolioapi.enums.ResourceType.IMAGE).folder("portfolio/upload/image").extension("png").build());

        // Assert
        assertNotNull(resultUrl);
        assertEquals(
                "https://res.cloudinary.com/test-cloud/image/upload/portfolio/upload/image/test-file.png",
                resultUrl
        );
        verify(mockUrl).secure(true);
        verify(mockUrl).format("png");
        verify(mockUrl).generate("portfolio/upload/image/test-file");
    }

    @Test
    @DisplayName("delete - valid fileId - successfully invokes destroy")
    void delete_validFileId_invokesUploaderDestroy() throws Exception {
        // Arrange
        String fileId = "test-file.png";
        Uploader mockUploader = mock(Uploader.class);
        when(cloudinary.uploader()).thenReturn(mockUploader);
        when(mockUploader.destroy(anyString(), any(Map.class))).thenReturn(Map.of("result", "ok"));

        // Act
        cloudinaryClient.delete(FileMetaData.builder().fileName("test-file").accessType(io.github.yubrajsahoo.portfolioapi.enums.AccessType.PUBLIC).resourceType(io.github.yubrajsahoo.portfolioapi.enums.ResourceType.IMAGE).folder("portfolio/upload/image").extension("png").build());

        // Assert
        verify(cloudinary, times(1)).uploader();
        verify(mockUploader, times(1))
                .destroy(eq("portfolio/upload/image/test-file"), any(Map.class));
    }

    @Test
    @DisplayName("delete - null fileId - throws CloudinaryException")
    void delete_nullFileId_throwsCloudinaryException() {
        // Act & Assert
        CloudinaryException exception = assertThrows(
                CloudinaryException.class, () ->
                        cloudinaryClient.delete(FileMetaData.builder().fileName(null
                        ).accessType(io.github.yubrajsahoo.portfolioapi.enums.AccessType.PUBLIC).resourceType(io.github.yubrajsahoo.portfolioapi.enums.ResourceType.IMAGE).folder("portfolio/upload/image").extension("png").build())
        );
        assertEquals("Exception occurred while deleting file from Cloudinary: null", exception.getMessage());
    }

    @Test
    @DisplayName("delete - CloudinaryException from uploader - propagates Exception")
    void delete_cloudinaryException_propagatesException() throws Exception {
        // Arrange
        String fileId = "test-file.png";
        Uploader mockUploader = mock(Uploader.class);
        when(cloudinary.uploader()).thenReturn(mockUploader);
        CloudinaryException cloudinaryException = new CloudinaryException("Delete failed");
        when(mockUploader.destroy(anyString(), any(Map.class))).thenThrow(cloudinaryException);

        // Act & Assert
        CloudinaryException exception = assertThrows(
                CloudinaryException.class, () ->
                        cloudinaryClient.delete(FileMetaData.builder().fileName(fileId
                        ).accessType(io.github.yubrajsahoo.portfolioapi.enums.AccessType.PUBLIC).resourceType(io.github.yubrajsahoo.portfolioapi.enums.ResourceType.IMAGE).folder("portfolio/upload/image").extension("png").build())
        );
        assertEquals("Exception occurred while deleting file from Cloudinary: " + fileId, exception.getMessage());
    }

    @Test
    @DisplayName("delete - generic Exception from uploader - wraps and throws CloudinaryException")
    void delete_genericException_throwsCloudinaryException() throws Exception {
        // Arrange
        String fileId = "test-file.png";
        Uploader mockUploader = mock(Uploader.class);
        when(cloudinary.uploader()).thenReturn(mockUploader);
        RuntimeException genericException = new RuntimeException("Generic error");
        when(mockUploader.destroy(anyString(), any(Map.class))).thenThrow(genericException);

        // Act & Assert
        CloudinaryException exception = assertThrows(
                CloudinaryException.class, () ->
                        cloudinaryClient.delete(FileMetaData.builder().fileName(fileId
                        ).accessType(io.github.yubrajsahoo.portfolioapi.enums.AccessType.PUBLIC).resourceType(io.github.yubrajsahoo.portfolioapi.enums.ResourceType.IMAGE).folder("portfolio/upload/image").extension("png").build())
        );
        assertEquals("Exception occurred while deleting file from Cloudinary: " + fileId, exception.getMessage());
        assertEquals(genericException, exception.getCause());
    }

    @Test
    @DisplayName("delete private - valid fileId - successfully invokes destroy")
    void deletePrivate_validFileId_invokesUploaderDestroy() throws Exception {
        // Arrange
        String fileId = "test-document.pdf";
        Uploader mockUploader = mock(Uploader.class);
        when(cloudinary.uploader()).thenReturn(mockUploader);
        when(mockUploader.destroy(anyString(), any(Map.class))).thenReturn(Map.of("result", "ok"));

        // Act
        cloudinaryClient.delete(FileMetaData.builder().fileName("test-file").accessType(io.github.yubrajsahoo.portfolioapi.enums.AccessType.PRIVATE).resourceType(io.github.yubrajsahoo.portfolioapi.enums.ResourceType.IMAGE).folder("portfolio/upload/image").extension("png").build());

        // Assert
        verify(cloudinary, times(1)).uploader();
        verify(mockUploader, times(1)).destroy(anyString(), any(Map.class));
    }


}








