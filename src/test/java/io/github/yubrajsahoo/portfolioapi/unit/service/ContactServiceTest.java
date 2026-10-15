/*
 *
 *  * Copyright (c) 2026 Yubraj Sahoo. All rights reserved.
 *
 */

package io.github.yubrajsahoo.portfolioapi.unit.service;

import io.github.yubrajsahoo.portfolioapi.dto.ContactMessageDto;
import io.github.yubrajsahoo.portfolioapi.dto.ContactReqDto;
import io.github.yubrajsahoo.portfolioapi.dto.ContactResDto;
import io.github.yubrajsahoo.portfolioapi.entity.ContactMessage;
import io.github.yubrajsahoo.portfolioapi.mapper.CustomMapper;
import io.github.yubrajsahoo.portfolioapi.repository.ContactMessageRepository;
import io.github.yubrajsahoo.portfolioapi.service.ContactService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest
@ActiveProfiles("unit")
@DisplayName("Unit: Contact Service Operations")
class ContactServiceTest {

    @MockitoBean
    private ContactMessageRepository contactMessageRepository;

    @MockitoBean
    private CustomMapper customMapper;

    @MockitoBean
    private io.github.yubrajsahoo.portfolioapi.service.EmailService emailService;

    @Autowired
    private ContactService contactService;

    @Test
    @DisplayName("Should successfully submit a contact message")
    void submitContactMessage_Success() {
        ContactReqDto reqDto = ContactReqDto.builder()
                .name("John Doe")
                .email("john@example.com")
                .message("I would like to discuss a project.")
                .build();

        ContactMessage entity = ContactMessage.builder()
                .name("John Doe")
                .email("john@example.com")
                .message("I would like to discuss a project.")
                .build();

        ContactMessage savedEntity = ContactMessage.builder()
                .id(1L)
                .name("John Doe")
                .email("john@example.com")
                .message("I would like to discuss a project.")
                .build();

        when(customMapper.toContactMessageEntity(reqDto)).thenReturn(entity);
        when(contactMessageRepository.save(entity)).thenReturn(savedEntity);

        ContactResDto response = contactService.submitContactMessage(reqDto);

        assertNotNull(response);
        assertTrue(response.isSuccess());
        assertEquals("Thank you for reaching out! Your message has been received.", response.getMessage());

        verify(customMapper, times(1)).toContactMessageEntity(reqDto);
        verify(contactMessageRepository, times(1)).save(entity);
        verify(emailService, times(1)).sendContactNotificationEmail(reqDto);
    }

    @Test
    @DisplayName("Should successfully retrieve all contact messages")
    void getAllContactMessages_Success() {
        LocalDateTime now = LocalDateTime.now();
        ContactMessage message = ContactMessage.builder()
                .id(1L)
                .name("Jane Doe")
                .email("jane@example.com")
                .message("Hello")
                .isRead(false)
                .build();

        ContactMessageDto messageDto = ContactMessageDto.builder()
                .id(1L)
                .name("Jane Doe")
                .email("jane@example.com")
                .message("Hello")
                .isRead(false)
                .createdAt(now)
                .build();

        when(contactMessageRepository.findAllByOrderByCreatedAtDesc()).thenReturn(List.of(message));
        when(customMapper.toContactMessageDto(message)).thenReturn(messageDto);

        List<ContactMessageDto> result = contactService.getAllContactMessages();

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("Jane Doe", result.get(0).getName());
        assertEquals("jane@example.com", result.get(0).getEmail());

        verify(contactMessageRepository, times(1)).findAllByOrderByCreatedAtDesc();
        verify(customMapper, times(1)).toContactMessageDto(message);
    }
}
