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
}
