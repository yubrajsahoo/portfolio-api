/*
 *
 *  * Copyright (c) 2026 Yubraj Sahoo. All rights reserved.
 *
 */

package io.github.yubrajsahoo.portfolioapi.unit.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.yubrajsahoo.portfolioapi.dto.ContactMessageDto;
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

import java.time.LocalDateTime;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@DisplayName("Unit: Contact Controller Endpoints")
@ActiveProfiles("unit")
@WithMockUser(authorities = {"WRITE_PRIVILEGE", "READ_PRIVILEGE", "DELETE_PRIVILEGE"})
class ContactControllerTest {

    @Autowired
    private WebApplicationContext context;

    private MockMvc mockMvc;

    @MockitoBean
    private ContactService contactService;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @BeforeEach
    void setup() {
        mockMvc = MockMvcBuilders.webAppContextSetup(context).build();
    }

    @Test
    @DisplayName("Should successfully submit a contact message and return 201 Created")
    void submitContactMessage_Success() throws Exception {
        ContactReqDto reqDto = ContactReqDto.builder()
                .name("Yubraj")
                .email("yubraj@example.com")
                .message("Hello from the portfolio contact form!")
                .build();

        ContactResDto resDto = ContactResDto.builder()
                .success(true)
                .message("Thank you for reaching out! Your message has been received.")
                .build();

        when(contactService.submitContactMessage(any(ContactReqDto.class))).thenReturn(resDto);

        mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/contact")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(reqDto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.message").value("Thank you for reaching out! Your message has been received."));

        verify(contactService, times(1)).submitContactMessage(any(ContactReqDto.class));
    }

    @Test
    @DisplayName("Should return 400 Bad Request when contact form has invalid email")
    void submitContactMessage_InvalidEmail() throws Exception {
        ContactReqDto reqDto = ContactReqDto.builder()
                .name("Yubraj")
                .email("not-an-email")
                .message("Hello!")
                .build();

        mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/contact")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(reqDto)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.title").value("Bad Request"))
                .andExpect(jsonPath("$.errors.email").value("Invalid Email Id"));

        verify(contactService, never()).submitContactMessage(any(ContactReqDto.class));
    }

    @Test
    @DisplayName("Should return 400 Bad Request when contact form has blank fields")
    void submitContactMessage_BlankFields() throws Exception {
        ContactReqDto reqDto = ContactReqDto.builder()
                .name("")
                .email("")
                .message("")
                .build();

        mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/contact")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(reqDto)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.title").value("Bad Request"));

        verify(contactService, never()).submitContactMessage(any(ContactReqDto.class));
    }

    @Test
    @DisplayName("Should successfully retrieve all contact messages")
    void getAllContactMessages_Success() throws Exception {
        ContactMessageDto messageDto = ContactMessageDto.builder()
                .id(1L)
                .name("Alice")
                .email("alice@example.com")
                .message("Great portfolio!")
                .isRead(false)
                .createdAt(LocalDateTime.now())
                .build();

        when(contactService.getAllContactMessages()).thenReturn(List.of(messageDto));

        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/contact")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value("Alice"))
                .andExpect(jsonPath("$[0].email").value("alice@example.com"))
                .andExpect(jsonPath("$[0].message").value("Great portfolio!"));

        verify(contactService, times(1)).getAllContactMessages();
    }
}
