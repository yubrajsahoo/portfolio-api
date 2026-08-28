package io.github.yubrajsahoo.portfolioapi.integration.controller;

import io.github.yubrajsahoo.portfolioapi.domain.FileMetaData;
import io.github.yubrajsahoo.portfolioapi.helper.DataBuilderUtils;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.io.InputStream;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * End-to-end Integration tests for {@link io.github.yubrajsahoo.portfolioapi.controller.CloudFileController}.
 * <p>
 * This class runs without ANY mocks. It loads the full Spring Application Context,
 * hits the REST endpoints natively via MockMvc, and interacts with the real Cloudinary environment.
 */
@Slf4j
@SpringBootTest
@DisplayName("Integration: Cloud File Controller Endpoints")
public class CloudFileControllerIntegrationTest {

    @Autowired
    private WebApplicationContext context;

    private MockMvc mockMvc;

    @BeforeEach
    void setup() {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(context).build();
    }

    @Test
    @DisplayName("Should Return 201 Created When Uploading Public File (Integration)")
    @Disabled("Due to storing data in Cloudinary")
    void testUpload_Public() throws Exception {
        // Build reference data purely for testing/logging using the requested JSON strategy
        FileMetaData expectedMeta = DataBuilderUtils.readFromJson(
                "src/test/resources/json/file-meta-data-public-png-logo.json",
                FileMetaData.class
        );
        log.info("Testing upload for expected structure from JSON: {}", expectedMeta.getFileName());

        InputStream inputStream = DataBuilderUtils.readFile("src/test/resources/images/logo.png");
        // Note: MockMultipartFile is a Spring test utility to simulate form-data, not a Mockito mock.
        MockMultipartFile file = new MockMultipartFile("file", "logo.png", "image/png", inputStream);

        mockMvc.perform(MockMvcRequestBuilders.multipart("/api/v1/files")
                        .file(file)
                        .param("access", "PUBLIC"))
                .andExpect(status().isCreated());
    }

    @Test
    @DisplayName("Should Return 200 OK with URL for Public File (Integration)")
    void testGetUrl_Public() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/files/logo.png")
                        .param("access", "PUBLIC"))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("Should Return 200 OK with URL for Private File (Integration)")
    @Disabled("Due to hitting Cloudinary")
    void testGetUrl_Private() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/files/Yubraj-Resume.pdf")
                        .param("access", "PRIVATE"))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("Should Return 202 Accepted When Deleting Public File (Integration)")
    @Disabled("Due to hitting Cloudinary")
    void testDelete_Public() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.delete("/api/v1/files/logo.png")
                        .param("access", "PUBLIC"))
                .andExpect(status().isAccepted());
    }

    @Test
    @DisplayName("Should Return 200 OK with List of All Public Files (Integration)")
    void testGetAllFileNames_Public() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/files")
                        .param("access", "PUBLIC"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray());
    }

    @Test
    @DisplayName("Should Return 200 OK with List of All Private Files (Integration)")
    void testGetAllFileNames_Private() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/files")
                        .param("access", "PRIVATE"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray());
    }
}
