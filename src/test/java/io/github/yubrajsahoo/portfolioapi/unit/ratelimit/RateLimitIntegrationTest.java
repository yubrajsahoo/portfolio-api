/*
 *
 *  * Copyright (c) 2026 Yubraj Sahoo. All rights reserved.
 *
 */

package io.github.yubrajsahoo.portfolioapi.unit.ratelimit;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.yubrajsahoo.portfolioapi.client.impl.CloudinaryClient;
import io.github.yubrajsahoo.portfolioapi.dto.ContactReqDto;
import io.github.yubrajsahoo.portfolioapi.dto.ContactResDto;
import io.github.yubrajsahoo.portfolioapi.service.ContactService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@DisplayName("Integration: Per-IP Rate Limiting for Contact and Resume")
@ActiveProfiles("unit")
@WithMockUser(authorities = {"READ_PRIVILEGE", "WRITE_PRIVILEGE"})
class RateLimitIntegrationTest {

    @Autowired
    private WebApplicationContext context;

    @MockitoBean
    private ContactService contactService;

    @MockitoBean
    private CloudinaryClient cloudinaryClient;

    private MockMvc mockMvc;
    private final ObjectMapper objectMapper = new ObjectMapper();

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.webAppContextSetup(context).build();
    }

    @Test
    @DisplayName("Should enforce rate limit per IP for contact form submission using library @RateLimit")
    void submitContactMessage_RateLimitingPerIp() throws Exception {
        ContactReqDto reqDto = ContactReqDto.builder()
                .name("Test User")
                .email("test@example.com")
                .message("Hello")
                .build();

        when(contactService.submitContactMessage(any(ContactReqDto.class)))
                .thenReturn(ContactResDto.builder().success(true).message("Received").build());

        String testIp = "203.0.113.10";

        // Request 1: Allowed (Remaining = 2 out of 3)
        mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/contact")
                        .header("X-Forwarded-For", testIp)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(reqDto)))
                .andExpect(status().isCreated())
                .andExpect(header().string("X-RateLimit-Limit", "3"))
                .andExpect(header().string("X-RateLimit-Remaining", "2"));

        // Request 2: Allowed (Remaining = 1)
        mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/contact")
                        .header("X-Forwarded-For", testIp)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(reqDto)))
                .andExpect(status().isCreated())
                .andExpect(header().string("X-RateLimit-Remaining", "1"));

        // Request 3: Allowed (Remaining = 0)
        mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/contact")
                        .header("X-Forwarded-For", testIp)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(reqDto)))
                .andExpect(status().isCreated())
                .andExpect(header().string("X-RateLimit-Remaining", "0"));

        // Request 4: Blocked (429 Too Many Requests)
        mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/contact")
                        .header("X-Forwarded-For", testIp)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(reqDto)))
                .andExpect(status().isTooManyRequests())
                .andExpect(header().exists("Retry-After"));

        // Different IP: Still Allowed
        String differentIp = "203.0.113.99";
        mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/contact")
                        .header("X-Forwarded-For", differentIp)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(reqDto)))
                .andExpect(status().isCreated());
    }

    @Test
    @DisplayName("Should enforce rate limit per IP for downloading resume / file URL using library @RateLimit")
    void downloadResume_RateLimitingPerIp() throws Exception {
        when(cloudinaryClient.getUrl(any())).thenReturn("https://res.cloudinary.com/demo/image/upload/resume.pdf");

        String testIp = "198.51.100.25";

        // Perform 5 allowed requests (Limit = 5)
        for (int i = 0; i < 5; i++) {
            mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/files/Yubraj-Resume.pdf")
                            .header("X-Forwarded-For", testIp)
                            .param("access", "PRIVATE"))
                    .andExpect(status().isOk())
                    .andExpect(header().string("X-RateLimit-Limit", "5"));
        }

        // Request 6: Blocked (429 Too Many Requests)
        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/files/Yubraj-Resume.pdf")
                        .header("X-Forwarded-For", testIp)
                        .param("access", "PRIVATE"))
                .andExpect(status().isTooManyRequests())
                .andExpect(header().exists("Retry-After"));
    }
}
