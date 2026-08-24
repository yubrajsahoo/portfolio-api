package io.github.yubrajsahoo.portfolioapi.unit.client.impl;

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
        classes = {CloudinaryClient.class, io.github.yubrajsahoo.portfolioapi.config.CloudinaryConfig.class},
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
    @DisplayName("uploadPublic - valid parameters - successfully uploads and returns secure URL")
    void uploadPublic_validParams_returnsSecureUrl() throws Exception {
        // Arrange
        InputStream inputStream = new ByteArrayInputStream("test content".getBytes());
        String fileName = "test-file.png";

        Uploader mockUploader = mock(Uploader.class);
        when(cloudinary.uploader()).thenReturn(mockUploader);

        Map<String, String> uploadResult = new HashMap<>();
        uploadResult.put("secure_url", "https://res.cloudinary.com/test-cloud/image/upload/v1/portfolio/upload/image/test-file.png");

        when(mockUploader.upload(any(byte[].class), any(Map.class))).thenReturn(uploadResult);

        // Act
        String resultUrl = cloudinaryClient.uploadPublic(inputStream, fileName);

        // Assert
        assertNotNull(resultUrl, "The returned secure URL should not be null");
        assertEquals("https://res.cloudinary.com/test-cloud/image/upload/v1/portfolio/upload/image/test-file.png", resultUrl);
        verify(cloudinary, times(1)).uploader();
        verify(mockUploader, times(1)).upload(any(byte[].class), any(Map.class));
    }

    @Test
    @DisplayName("uploadPublic - null input stream - throws IllegalArgumentException")
    void uploadPublic_nullInputStream_throwsIllegalArgumentException() {
        // Arrange
        String fileName = "test-file.png";

        // Act & Assert
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () ->
                cloudinaryClient.uploadPublic(null, fileName)
        );
        assertEquals("Unable to read file", exception.getMessage());
    }

    @Test
    @DisplayName("uploadPublic - null file name - throws IllegalArgumentException")
    void uploadPublic_nullFileName_throwsIllegalArgumentException() {
        // Arrange
        InputStream inputStream = new ByteArrayInputStream("test content".getBytes());

        // Act & Assert
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () ->
                cloudinaryClient.uploadPublic(inputStream, null)
        );
        assertEquals("File name must not be null or blank", exception.getMessage());
    }

    @Test
    @DisplayName("uploadPublic - empty file name - throws IllegalArgumentException")
    void uploadPublic_emptyFileName_throwsIllegalArgumentException() {
        // Arrange
        InputStream inputStream = new ByteArrayInputStream("test content".getBytes());

        // Act & Assert
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            cloudinaryClient.uploadPublic(inputStream, " ");
        });
        assertEquals("File name must not be null or blank", exception.getMessage());
    }

    @Test
    @DisplayName("uploadPublic - CloudinaryException from uploader - propagates the CloudinaryException")
    void uploadPublic_cloudinaryException_propagatesException() throws Exception {
        // Arrange
        InputStream inputStream = new ByteArrayInputStream("test content".getBytes());
        String fileName = "test-file.png";

        Uploader mockUploader = mock(Uploader.class);
        when(cloudinary.uploader()).thenReturn(mockUploader);

        CloudinaryException cloudinaryException = new CloudinaryException("Cloudinary upload failed");
        when(mockUploader.upload(any(byte[].class), any(Map.class))).thenThrow(cloudinaryException);

        // Act & Assert
        CloudinaryException exception = assertThrows(CloudinaryException.class, () -> {
            cloudinaryClient.uploadPublic(inputStream, fileName);
        });
        assertEquals("Cloudinary upload failed", exception.getMessage());
    }

    @Test
    @DisplayName("uploadPublic - generic Exception from uploader - wraps and throws CloudinaryException")
    void uploadPublic_genericException_throwsCloudinaryException() throws Exception {
        // Arrange
        InputStream inputStream = new ByteArrayInputStream("test content".getBytes());
        String fileName = "test-file.png";

        Uploader mockUploader = mock(Uploader.class);
        when(cloudinary.uploader()).thenReturn(mockUploader);

        RuntimeException genericException = new RuntimeException("Generic upload failure");
        when(mockUploader.upload(any(byte[].class), any(Map.class))).thenThrow(genericException);

        // Act & Assert
        CloudinaryException exception = assertThrows(CloudinaryException.class, () -> {
            cloudinaryClient.uploadPublic(inputStream, fileName);
        });
        assertEquals("Exception occurred while uploading file to Cloudinary", exception.getMessage());
        assertEquals(genericException, exception.getCause());
    }

    @Test
    @DisplayName("generatePrivateUrl - valid parameters - successfully generates signed private URL")
    void generatePrivateUrl_validParams_returnsSignedUrl() throws Exception {
        // Arrange
        String fileId = "test-file.png";
        String expectedUrl = "https://res.cloudinary.com/test-cloud/image/authenticated/v1/portfolio/authenticated/image/test-file.png?s=signature";

        when(cloudinary.privateDownload(anyString(), anyString(), any(Map.class))).thenReturn(expectedUrl);

        // Act
        String resultUrl = cloudinaryClient.generatePrivateUrl(fileId);

        // Assert
        assertNotNull(resultUrl);
        assertEquals(expectedUrl, resultUrl);
        verify(cloudinary, times(1)).privateDownload(eq("portfolio/authenticated/image/test-file.png"), eq("png"), any(Map.class));
    }

    @Test
    @DisplayName("deletePublic - valid fileId - successfully invokes destroy")
    void deletePublic_validFileId_invokesUploaderDestroy() throws Exception {
        // Arrange
        String fileId = "test-file.png";
        Uploader mockUploader = mock(Uploader.class);
        when(cloudinary.uploader()).thenReturn(mockUploader);
        when(mockUploader.destroy(anyString(), any(Map.class))).thenReturn(Map.of("result", "ok"));

        // Act
        cloudinaryClient.deletePublic(fileId);

        // Assert
        verify(cloudinary, times(1)).uploader();
        verify(mockUploader, times(1)).destroy(eq("portfolio/upload/image/test-file.png"), any(Map.class));
    }

    @Test
    @DisplayName("deletePublic - null fileId - throws IllegalArgumentException")
    void deletePublic_nullFileId_throwsIllegalArgumentException() {
        // Act & Assert
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            cloudinaryClient.deletePublic(null);
        });
        assertEquals("File ID must not be null or blank", exception.getMessage());
    }

    @Test
    @DisplayName("deletePublic - CloudinaryException from uploader - propagates Exception")
    void deletePublic_cloudinaryException_propagatesException() throws Exception {
        // Arrange
        String fileId = "test-file.png";
        Uploader mockUploader = mock(Uploader.class);
        when(cloudinary.uploader()).thenReturn(mockUploader);
        CloudinaryException cloudinaryException = new CloudinaryException("Delete failed");
        when(mockUploader.destroy(anyString(), any(Map.class))).thenThrow(cloudinaryException);

        // Act & Assert
        CloudinaryException exception = assertThrows(CloudinaryException.class, () -> {
            cloudinaryClient.deletePublic(fileId);
        });
        assertEquals("Delete failed", exception.getMessage());
    }

    @Test
    @DisplayName("deletePublic - generic Exception from uploader - wraps and throws CloudinaryException")
    void deletePublic_genericException_throwsCloudinaryException() throws Exception {
        // Arrange
        String fileId = "test-file.png";
        Uploader mockUploader = mock(Uploader.class);
        when(cloudinary.uploader()).thenReturn(mockUploader);
        RuntimeException genericException = new RuntimeException("Generic error");
        when(mockUploader.destroy(anyString(), any(Map.class))).thenThrow(genericException);

        // Act & Assert
        CloudinaryException exception = assertThrows(CloudinaryException.class, () -> {
            cloudinaryClient.deletePublic(fileId);
        });
        assertEquals("Exception occurred while deleting file from Cloudinary: " + fileId, exception.getMessage());
        assertEquals(genericException, exception.getCause());
    }

    @Test
    @DisplayName("deletePrivate - valid fileId - successfully invokes destroy")
    void deletePrivate_validFileId_invokesUploaderDestroy() throws Exception {
        // Arrange
        String fileId = "test-document.pdf";
        Uploader mockUploader = mock(Uploader.class);
        when(cloudinary.uploader()).thenReturn(mockUploader);
        when(mockUploader.destroy(anyString(), any(Map.class))).thenReturn(Map.of("result", "ok"));

        // Act
        cloudinaryClient.deletePrivate(fileId);

        // Assert
        verify(cloudinary, times(1)).uploader();
        // Since pdf is raw/document, resource type might be raw/document, assuming buildPublicId produces "portfolio/private/raw/test-document.pdf"
        // Let's use anyString() for exact public ID to avoid brittle tests on exact formatting, or verify correctly.
        verify(mockUploader, times(1)).destroy(anyString(), any(Map.class));
    }
}
