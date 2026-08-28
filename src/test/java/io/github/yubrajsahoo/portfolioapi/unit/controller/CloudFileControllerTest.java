package io.github.yubrajsahoo.portfolioapi.unit.controller;

import io.github.yubrajsahoo.portfolioapi.client.impl.CloudinaryClient;
import io.github.yubrajsahoo.portfolioapi.domain.FileMetaData;
import io.github.yubrajsahoo.portfolioapi.enums.AccessType;
import io.github.yubrajsahoo.portfolioapi.helper.DataBuilderUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.io.InputStream;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@DisplayName("Unit: Cloud File Controller Endpoints")
class CloudFileControllerTest {

    @Autowired
    private WebApplicationContext context;

    private MockMvc mockMvc;

    @MockitoBean
    private CloudinaryClient cloudinaryClient;

    @BeforeEach
    void setup() {
        mockMvc = MockMvcBuilders.webAppContextSetup(context).build();
    }

    @Test
    @DisplayName("Should Return 201 Created Upon Successful File Upload")
    void testUpload_Public() throws Exception {
        InputStream inputStream = DataBuilderUtils.readFile("src/test/resources/images/logo.png");
        MockMultipartFile file = new MockMultipartFile("file", "logo.png", "image/png", inputStream);

        String mockUrl = "https://res.cloudinary.com/demo/image/upload/portfolio-api/public/image/logo.png";
        when(cloudinaryClient.upload(any(InputStream.class), any(FileMetaData.class))).thenReturn(mockUrl);

        mockMvc.perform(MockMvcRequestBuilders.multipart("/api/v1/files")
                        .file(file)
                        .param("access", "PUBLIC"))
                .andExpect(status().isCreated())
                .andExpect(content().string(mockUrl));
    }

    @Test
    @DisplayName("Should Return 200 OK with File Download URL")
    void testGetUrl_Public() throws Exception {
        String mockUrl = "https://res.cloudinary.com/demo/image/upload/portfolio-api/public/image/logo.png";
        when(cloudinaryClient.getUrl(any(FileMetaData.class))).thenReturn(mockUrl);

        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/files/logo.png")
                        .param("access", "PUBLIC"))
                .andExpect(status().isOk())
                .andExpect(content().string("\"" + mockUrl + "\""));
    }

    @Test
    @DisplayName("Should Return 202 Accepted Upon Successful File Deletion")
    void testDelete_Public() throws Exception {
        doNothing().when(cloudinaryClient).delete(any(FileMetaData.class));

        mockMvc.perform(MockMvcRequestBuilders.delete("/api/v1/files/logo.png")
                        .param("access", "PUBLIC"))
                .andExpect(status().isAccepted());

        verify(cloudinaryClient, times(1)).delete(any(FileMetaData.class));
    }

    @Test
    @DisplayName("Should Return 200 OK with All Available Cloud Files")
    void testGetAllFileNames() throws Exception {
        String mockUrl = "https://res.cloudinary.com/demo/image/upload/v1/portfolio/public/image/logo.png";
        when(cloudinaryClient.getAllUrls(AccessType.PUBLIC)).thenReturn(List.of(mockUrl));

        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/files")
                        .param("access", "PUBLIC"))
                .andExpect(status().isOk())
                .andExpect(content().json("[{\"fileName\":\"logo.png\",\"url\":\"" + mockUrl + "\",\"type\":\"IMAGE\",\"access\":\"PUBLIC\",\"project\":\"portfolio\"}]"));
    }
}
