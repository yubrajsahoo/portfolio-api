/*
 *
 *  * Copyright (c) 2026 Yubraj Sahoo. All rights reserved.
 *
 */

package io.github.yubrajsahoo.portfolioapi.unit.client;

import com.cloudinary.Cloudinary;
import com.cloudinary.Uploader;
import io.github.yubrajsahoo.portfolioapi.client.impl.CloudinaryClient;
import io.github.yubrajsahoo.portfolioapi.domain.FileMetaData;
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
@DisplayName("Unit: Cloudinary Client Operations")
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
        FileMetaData metaData = FileMetaData.builder().fileName(fileName).accessType(io.github.yubrajsahoo.portfolioapi.enums.AccessType.PUBLIC).resourceType(io.github.yubrajsahoo.portfolioapi.enums.ResourceType.IMAGE).folder("portfolio/upload/image").extension("png").build();
        CloudinaryException exception = assertThrows(CloudinaryException.class, () ->
                cloudinaryClient.upload(null
                        , metaData)
        );
        assertEquals("Exception occurred while uploading file to Cloudinary", exception.getMessage());
    }

    @Test
    @DisplayName("upload - null file name - throws CloudinaryException")
    void upload_nullFileName_throwsCloudinaryException() {
        // Arrange
        InputStream inputStream = new ByteArrayInputStream("test content".getBytes());

        // Act & Assert
        FileMetaData metaData = FileMetaData.builder().fileName(null).accessType(io.github.yubrajsahoo.portfolioapi.enums.AccessType.PUBLIC).resourceType(io.github.yubrajsahoo.portfolioapi.enums.ResourceType.IMAGE).folder("portfolio/upload/image").extension("png").build();
        CloudinaryException exception = assertThrows(CloudinaryException.class, () ->
                cloudinaryClient.upload(inputStream
                        , metaData)
        );
        assertEquals("Exception occurred while uploading file to Cloudinary", exception.getMessage());
    }

    @Test
    @DisplayName("upload - empty file name - throws CloudinaryException")
    void upload_emptyFileName_throwsCloudinaryException() {
        // Arrange
        InputStream inputStream = new ByteArrayInputStream("test content".getBytes());

        // Act & Assert
        FileMetaData metaData = FileMetaData.builder().fileName(" ").accessType(io.github.yubrajsahoo.portfolioapi.enums.AccessType.PUBLIC).resourceType(io.github.yubrajsahoo.portfolioapi.enums.ResourceType.IMAGE).folder("portfolio/upload/image").extension("png").build();
        CloudinaryException exception = assertThrows(
                CloudinaryException.class, () ->
                        cloudinaryClient.upload(inputStream
                                , metaData)
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
        FileMetaData metaData = FileMetaData.builder().fileName(fileName).accessType(io.github.yubrajsahoo.portfolioapi.enums.AccessType.PUBLIC).resourceType(io.github.yubrajsahoo.portfolioapi.enums.ResourceType.IMAGE).folder("portfolio/upload/image").extension("png").build();
        CloudinaryException exception = assertThrows(
                CloudinaryException.class, () ->
                        cloudinaryClient.upload(inputStream
                                , metaData)
        );
        assertEquals("Exception occurred while uploading file to Cloudinary", exception.getMessage());
    }

    @Test
    @DisplayName("Should Wrap Generic Error into CloudinaryException During Upload")
    void upload_genericException_throwsCloudinaryException() throws Exception {
        // Arrange
        InputStream inputStream = new ByteArrayInputStream("test content".getBytes());
        String fileName = "test-file.png";

        Uploader mockUploader = mock(Uploader.class);
        when(cloudinary.uploader()).thenReturn(mockUploader);

        RuntimeException genericException = new RuntimeException("Generic upload failure");
        when(mockUploader.upload(any(byte[].class), any(Map.class))).thenThrow(genericException);

        // Act & Assert
        FileMetaData metaData = FileMetaData.builder().fileName(fileName).accessType(io.github.yubrajsahoo.portfolioapi.enums.AccessType.PUBLIC).resourceType(io.github.yubrajsahoo.portfolioapi.enums.ResourceType.IMAGE).folder("portfolio/upload/image").extension("png").build();
        CloudinaryException exception = assertThrows(
                CloudinaryException.class, () ->
                        cloudinaryClient.upload(inputStream
                                , metaData)
        );
        assertEquals("Exception occurred while uploading file to Cloudinary", exception.getMessage());
        assertEquals(genericException, exception.getCause());
    }

    @Test
    @DisplayName("Should Generate Signed URL For Private Files")
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
    @DisplayName("Should Generate Secure URL For Public Files")
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
    @DisplayName("Should Successfully Delete Public File from Cloudinary")
    void delete_validFileId_invokesUploaderDestroy() throws Exception {
        // Arrange
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
    @DisplayName("Should Throw Exception When Deleting File with Null ID")
    void delete_nullFileId_throwsCloudinaryException() {
        // Act & Assert
        FileMetaData metaData = FileMetaData.builder().fileName(null
                        ).accessType(io.github.yubrajsahoo.portfolioapi.enums.AccessType.PUBLIC).resourceType(io.github.yubrajsahoo.portfolioapi.enums.ResourceType.IMAGE).folder("portfolio/upload/image").extension("png").build();
        CloudinaryException exception = assertThrows(
                CloudinaryException.class, () ->
                        cloudinaryClient.delete(metaData)
        );
        assertEquals("Exception occurred while deleting file from Cloudinary: null", exception.getMessage());
    }

    @Test
    @DisplayName("Should Propagate Cloudinary Error During File Deletion")
    void delete_cloudinaryException_propagatesException() throws Exception {
        // Arrange
        String fileId = "test-file.png";
        Uploader mockUploader = mock(Uploader.class);
        when(cloudinary.uploader()).thenReturn(mockUploader);
        CloudinaryException cloudinaryException = new CloudinaryException("Delete failed");
        when(mockUploader.destroy(anyString(), any(Map.class))).thenThrow(cloudinaryException);

        // Act & Assert
        FileMetaData metaData = FileMetaData.builder().fileName(fileId
                        ).accessType(io.github.yubrajsahoo.portfolioapi.enums.AccessType.PUBLIC).resourceType(io.github.yubrajsahoo.portfolioapi.enums.ResourceType.IMAGE).folder("portfolio/upload/image").extension("png").build();
        CloudinaryException exception = assertThrows(
                CloudinaryException.class, () ->
                        cloudinaryClient.delete(metaData)
        );
        assertEquals("Exception occurred while deleting file from Cloudinary: " + fileId, exception.getMessage());
    }

    @Test
    @DisplayName("Should Wrap Generic Error into CloudinaryException During Deletion")
    void delete_genericException_throwsCloudinaryException() throws Exception {
        // Arrange
        String fileId = "test-file.png";
        Uploader mockUploader = mock(Uploader.class);
        when(cloudinary.uploader()).thenReturn(mockUploader);
        RuntimeException genericException = new RuntimeException("Generic error");
        when(mockUploader.destroy(anyString(), any(Map.class))).thenThrow(genericException);

        // Act & Assert
        FileMetaData metaData = FileMetaData.builder().fileName(fileId
                        ).accessType(io.github.yubrajsahoo.portfolioapi.enums.AccessType.PUBLIC).resourceType(io.github.yubrajsahoo.portfolioapi.enums.ResourceType.IMAGE).folder("portfolio/upload/image").extension("png").build();
        CloudinaryException exception = assertThrows(
                CloudinaryException.class, () ->
                        cloudinaryClient.delete(metaData)
        );
        assertEquals("Exception occurred while deleting file from Cloudinary: " + fileId, exception.getMessage());
        assertEquals(genericException, exception.getCause());
    }

    @Test
    @DisplayName("Should Successfully Delete Private File from Cloudinary")
    void deletePrivate_validFileId_invokesUploaderDestroy() throws Exception {
        // Arrange
        Uploader mockUploader = mock(Uploader.class);
        when(cloudinary.uploader()).thenReturn(mockUploader);
        when(mockUploader.destroy(anyString(), any(Map.class))).thenReturn(Map.of("result", "ok"));

        // Act
        cloudinaryClient.delete(FileMetaData.builder().fileName("test-file").accessType(io.github.yubrajsahoo.portfolioapi.enums.AccessType.PRIVATE).resourceType(io.github.yubrajsahoo.portfolioapi.enums.ResourceType.IMAGE).folder("portfolio/upload/image").extension("png").build());

        // Assert
        verify(cloudinary, times(1)).uploader();
        verify(mockUploader, times(1)).destroy(anyString(), any(Map.class));
    }


    @Test
    @DisplayName("Should Successfully Retrieve All URLs for Given Access Type")
    void getAllUrls_validAccessType_returnsUrls() throws Exception {
        // Arrange
        com.cloudinary.Api mockApi = mock(com.cloudinary.Api.class);
        when(cloudinary.api()).thenReturn(mockApi);
        
        Map<String, Object> resource1 = new HashMap<>();
        resource1.put("secure_url", "https://res.cloudinary.com/test/image1.png");
        
        Map<String, Object> resource2 = new HashMap<>();
        resource2.put("url", "http://res.cloudinary.com/test/image2.png");
        
        Map<String, Object> resource3 = new HashMap<>();
        // No url or secure_url
        
        com.cloudinary.api.ApiResponse response = mock(com.cloudinary.api.ApiResponse.class);
        when(response.containsKey("resources")).thenReturn(true);
        when(response.get("resources")).thenReturn(java.util.Arrays.asList(resource1, resource2, resource3));
        
        when(mockApi.resources(any(Map.class))).thenReturn(response);
        
        // Act
        java.util.List<String> urls = cloudinaryClient.getAllUrls(io.github.yubrajsahoo.portfolioapi.enums.AccessType.PUBLIC);
        
        // Assert
        assertEquals(2, urls.size());
        assertTrue(urls.contains("https://res.cloudinary.com/test/image1.png"));
        assertTrue(urls.contains("http://res.cloudinary.com/test/image2.png"));
    }

    @Test
    @DisplayName("Should Throw Exception When getAllUrls API Call Fails")
    void getAllUrls_apiException_throwsCloudinaryException() throws Exception {
        // Arrange
        com.cloudinary.Api mockApi = mock(com.cloudinary.Api.class);
        when(cloudinary.api()).thenReturn(mockApi);
        when(mockApi.resources(any(Map.class))).thenThrow(new RuntimeException("API failure"));
        
        // Act & Assert
        CloudinaryException exception = assertThrows(
                CloudinaryException.class, () ->
                        cloudinaryClient.getAllUrls(io.github.yubrajsahoo.portfolioapi.enums.AccessType.PUBLIC)
        );
        assertEquals("Unable to fetch all files from Cloudinary", exception.getMessage());
    }

    @Test
    @DisplayName("Should Throw Exception When getAllUrls Response Is Invalid")
    void getAllUrls_invalidResponse_throwsCloudinaryException() throws Exception {
        // Arrange
        com.cloudinary.Api mockApi = mock(com.cloudinary.Api.class);
        when(cloudinary.api()).thenReturn(mockApi);
        com.cloudinary.api.ApiResponse response = mock(com.cloudinary.api.ApiResponse.class);
        when(response.containsKey("resources")).thenReturn(false);
        when(mockApi.resources(any(Map.class))).thenReturn(response); 
        
        // Act & Assert
        CloudinaryException exception = assertThrows(
                CloudinaryException.class, () ->
                        cloudinaryClient.getAllUrls(io.github.yubrajsahoo.portfolioapi.enums.AccessType.PUBLIC)
        );
        assertEquals("Not getting files from Cloudinary", exception.getMessage());
    }
}








