package io.github.yubrajsahoo.portfolioapi.unit.dto;

import io.github.yubrajsahoo.portfolioapi.dto.CloudFileDto;
import io.github.yubrajsahoo.portfolioapi.dto.PrivilegeReqDto;
import io.github.yubrajsahoo.portfolioapi.dto.RoleReqDto;
import io.github.yubrajsahoo.portfolioapi.dto.UserReqDto;
import io.github.yubrajsahoo.portfolioapi.enums.AccessType;
import io.github.yubrajsahoo.portfolioapi.enums.ResourceType;
import org.junit.jupiter.api.Test;

import java.net.URI;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class DtoTest {

    @Test
    void testCloudFileDto() {
        CloudFileDto dto = CloudFileDto.builder()
                .fileName("test")
                .type(ResourceType.IMAGE)
                .access(AccessType.PUBLIC)
                .application("portfolio")
                .url(URI.create("http://test.com"))
                .build();

        assertEquals("test", dto.getFileName());
        assertEquals(ResourceType.IMAGE, dto.getType());
        assertEquals(AccessType.PUBLIC, dto.getAccess());
        assertEquals("portfolio", dto.getApplication());
        assertEquals("http://test.com", dto.getUrl().toString());
    }

    @Test
    void testPrivilegeDto() {
        PrivilegeReqDto dto = new PrivilegeReqDto();
        dto.setName("READ");
        assertEquals("READ", dto.getName());
    }

    @Test
    void testRoleDto() {
        RoleReqDto dto = new RoleReqDto();
        dto.setName("USER");
        dto.setPrivileges(List.of("READ"));
        
        assertEquals("USER", dto.getName());
        assertEquals(1, dto.getPrivileges().size());
        assertEquals("READ", dto.getPrivileges().get(0));
    }

    @Test
    void testUserRegistrationDto() {
        UserReqDto dto = new UserReqDto();
        dto.setEmail("test@test.com");
        dto.setPassword("pass");
        dto.setRoles(List.of("USER"));

        assertEquals("test@test.com", dto.getEmail());
        assertEquals("pass", dto.getPassword());
        assertEquals(1, dto.getRoles().size());
        assertEquals("USER", dto.getRoles().get(0));
    }

    @Test
    void testContactReqDto() {
        io.github.yubrajsahoo.portfolioapi.dto.ContactReqDto dto = io.github.yubrajsahoo.portfolioapi.dto.ContactReqDto.builder()
                .name("John")
                .email("john@example.com")
                .message("Hello")
                .build();

        assertEquals("John", dto.getName());
        assertEquals("john@example.com", dto.getEmail());
        assertEquals("Hello", dto.getMessage());

        io.github.yubrajsahoo.portfolioapi.dto.ContactReqDto emptyDto = new io.github.yubrajsahoo.portfolioapi.dto.ContactReqDto();
        emptyDto.setName("Jane");
        emptyDto.setEmail("jane@example.com");
        emptyDto.setMessage("Hi");
        assertEquals("Jane", emptyDto.getName());
        assertEquals("jane@example.com", emptyDto.getEmail());
        assertEquals("Hi", emptyDto.getMessage());
    }

    @Test
    void testContactResDto() {
        io.github.yubrajsahoo.portfolioapi.dto.ContactResDto dto = io.github.yubrajsahoo.portfolioapi.dto.ContactResDto.builder()
                .success(true)
                .message("Success")
                .build();

        org.junit.jupiter.api.Assertions.assertTrue(dto.isSuccess());
        assertEquals("Success", dto.getMessage());

        io.github.yubrajsahoo.portfolioapi.dto.ContactResDto emptyDto = new io.github.yubrajsahoo.portfolioapi.dto.ContactResDto();
        emptyDto.setSuccess(false);
        emptyDto.setMessage("Failed");
        org.junit.jupiter.api.Assertions.assertFalse(emptyDto.isSuccess());
        assertEquals("Failed", emptyDto.getMessage());
    }

    @Test
    void testContactMessageDto() {
        java.time.LocalDateTime now = java.time.LocalDateTime.now();
        io.github.yubrajsahoo.portfolioapi.dto.ContactMessageDto dto = io.github.yubrajsahoo.portfolioapi.dto.ContactMessageDto.builder()
                .id(1L)
                .name("John")
                .email("john@example.com")
                .message("Test message")
                .isRead(true)
                .createdAt(now)
                .build();

        assertEquals(1L, dto.getId());
        assertEquals("John", dto.getName());
        assertEquals("john@example.com", dto.getEmail());
        assertEquals("Test message", dto.getMessage());
        org.junit.jupiter.api.Assertions.assertTrue(dto.isRead());
        assertEquals(now, dto.getCreatedAt());

        io.github.yubrajsahoo.portfolioapi.dto.ContactMessageDto emptyDto = new io.github.yubrajsahoo.portfolioapi.dto.ContactMessageDto();
        emptyDto.setId(2L);
        emptyDto.setName("Jane");
        emptyDto.setEmail("jane@example.com");
        emptyDto.setMessage("Msg");
        emptyDto.setRead(false);
        emptyDto.setCreatedAt(now);
        assertEquals(2L, emptyDto.getId());
        assertEquals("Jane", emptyDto.getName());
        assertEquals("jane@example.com", emptyDto.getEmail());
        assertEquals("Msg", emptyDto.getMessage());
        org.junit.jupiter.api.Assertions.assertFalse(emptyDto.isRead());
        assertEquals(now, emptyDto.getCreatedAt());
    }
}
