package io.github.yubrajsahoo.portfolioapi.unit.service;

import io.github.yubrajsahoo.portfolioapi.client.impl.CloudinaryClient;
import io.github.yubrajsahoo.portfolioapi.domain.FileMetaData;
import io.github.yubrajsahoo.portfolioapi.dto.CloudFileDto;
import io.github.yubrajsahoo.portfolioapi.enums.AccessType;
import io.github.yubrajsahoo.portfolioapi.exception.FileUploadException;
import io.github.yubrajsahoo.portfolioapi.mapper.impl.CustomMapperImpl;
import io.github.yubrajsahoo.portfolioapi.service.CloudFileService;
import io.github.yubrajsahoo.portfolioapi.service.impl.CloudFileServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;

import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@SpringBootTest
@ActiveProfiles("unit")
@DisplayName("Unit: Cloud File Service Operations")
class CloudFileServiceTest {

    @MockitoBean
    private CloudinaryClient cloudClient;

    @Autowired
    private CloudFileService cloudFileService;

    @Test
    @DisplayName("Should Successfully Upload File and Return Cloud URL")
    void upload_Success() throws Exception {
        InputStream inputStream = io.github.yubrajsahoo.portfolioapi.helper.DataBuilderUtils.readFile("src/test/resources/images/logo.png");
        MockMultipartFile file = new MockMultipartFile("file", "logo.png", "image/png", inputStream);
        when(cloudClient.upload(any(InputStream.class), any(FileMetaData.class))).thenReturn("uploaded_url");

        String result = cloudFileService.upload(file, AccessType.PUBLIC);

        assertEquals("uploaded_url", result);
        verify(cloudClient, times(1)).upload(any(InputStream.class), any(FileMetaData.class));
    }

    @Test
    @DisplayName("Should Throw FileUploadException on IOError During Upload")
    void upload_ThrowsFileUploadException() throws IOException {
        MockMultipartFile file = mock(MockMultipartFile.class);
        when(file.getOriginalFilename()).thenReturn("test.jpg");
        when(file.getInputStream()).thenThrow(new IOException("Test Exception"));

        assertThrows(FileUploadException.class, () -> cloudFileService.upload(file, AccessType.PUBLIC));
    }

    @Test
    @DisplayName("Should Generate Correct File URL via Client")
    void getUrl_Success() {
        when(cloudClient.getUrl(any(FileMetaData.class))).thenReturn("file_url");

        String result = cloudFileService.getUrl("test.jpg", AccessType.PUBLIC);

        assertEquals("file_url", result);
        verify(cloudClient, times(1)).getUrl(any(FileMetaData.class));
    }

    @Test
    @DisplayName("Should Delete File via Client Without Exception")
    void delete_Success() {
        doNothing().when(cloudClient).delete(any(FileMetaData.class));

        assertDoesNotThrow(() -> cloudFileService.delete("test.jpg", AccessType.PUBLIC));
        verify(cloudClient, times(1)).delete(any(FileMetaData.class));
    }

    @Test
    @DisplayName("Should Fetch and Map All File Names from Client Filters Nulls")
    void getAllFileNames_Success() {
        String mockUrl = "https://res.cloudinary.com/demo/image/upload/portfolio-api/public/image/test.jpg";
        List urls = io.github.yubrajsahoo.portfolioapi.helper.DataBuilderUtils.readFromJson(
                "src/test/resources/json/service-get-all-urls-public.json",
                List.class
        );
        when(cloudClient.getAllUrls(AccessType.PUBLIC)).thenReturn(urls);

        List<CloudFileDto> result = cloudFileService.getAllFileNames(AccessType.PUBLIC);

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("test.jpg", result.getFirst().getFileName());
        assertEquals(URI.create(mockUrl), result.getFirst().getUrl());
        assertEquals(AccessType.PUBLIC, result.getFirst().getAccess());
    }
}
