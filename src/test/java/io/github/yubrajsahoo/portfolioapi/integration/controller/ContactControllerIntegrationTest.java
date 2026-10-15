/*
 *
 *  * Copyright (c) 2026 Yubraj Sahoo. All rights reserved.
 *
 */

package io.github.yubrajsahoo.portfolioapi.integration.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.yubrajsahoo.portfolioapi.dto.ContactReqDto;
import io.github.yubrajsahoo.portfolioapi.entity.ContactMessage;
import io.github.yubrajsahoo.portfolioapi.repository.ContactMessageRepository;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * End-to-end Integration tests for {@link io.github.yubrajsahoo.portfolioapi.controller.ContactController}.
 * <p>
 * Runs without ANY mocks in the {@code dev} profile, storing real data into the database
 * and invoking the real email service.
 */
@Slf4j
@SpringBootTest
@DisplayName("Integration: Contact Controller Real Services (Dev Environment)")
@ActiveProfiles("dev")
class ContactControllerIntegrationTest {

    @Autowired
    private WebApplicationContext context;

    @Autowired
    private ContactMessageRepository contactMessageRepository;

    private MockMvc mockMvc;
    private final ObjectMapper objectMapper = new ObjectMapper();

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.webAppContextSetup(context).build();
    }

    @Test
    @DisplayName("Should submit contact message, store real data in database, and trigger real mail service")
    void submitContactMessage_RealDatabaseAndRealMail() throws Exception {
        String uniqueEmail = "integration.tester." + System.currentTimeMillis() + "@example.com";
        ContactReqDto reqDto = ContactReqDto.builder()
                .name("Integration Tester")
                .email(uniqueEmail)
                .message("This is a real integration test message stored in the database.")
                .build();

        long countBefore = contactMessageRepository.count();

        // Perform real POST request against the endpoint
        mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/contact")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(reqDto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.message").value("Thank you for reaching out! Your message has been received."));

        // Verify real database persistence
        long countAfter = contactMessageRepository.count();
        assertEquals(countBefore + 1, countAfter, "Database row count should increment by 1");

        List<ContactMessage> messages = contactMessageRepository.findByEmailOrderByCreatedAtDesc(uniqueEmail);
        assertFalse(messages.isEmpty(), "Persisted message must be found in real database");

        ContactMessage saved = messages.getFirst();
        assertNotNull(saved.getId(), "Generated ID must be assigned by the database");
        assertEquals("Integration Tester", saved.getName());
        assertEquals(uniqueEmail, saved.getEmail());
        assertEquals("This is a real integration test message stored in the database.", saved.getMessage());
        assertNotNull(saved.getCreatedAt(), "CreatedAt timestamp must be automatically populated");

        log.info("Successfully verified real database record with ID: {}", saved.getId());
    }

    @Test
    @DisplayName("Should retrieve all real contact messages from database when authenticated")
    @WithMockUser(authorities = {"READ_PRIVILEGE"})
    void getAllContactMessages_RealDatabase() throws Exception {
        // Ensure at least one real record exists
        ContactMessage message = ContactMessage.builder()
                .name("Existing Contact")
                .email("existing@example.com")
                .message("Existing database message")
                .build();
        contactMessageRepository.save(message);

        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/contact"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$[0].name").exists())
                .andExpect(jsonPath("$[0].email").exists());
    }
}
