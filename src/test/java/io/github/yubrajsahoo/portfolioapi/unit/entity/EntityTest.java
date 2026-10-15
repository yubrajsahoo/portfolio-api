package io.github.yubrajsahoo.portfolioapi.unit.entity;

import io.github.yubrajsahoo.portfolioapi.entity.Privilege;
import io.github.yubrajsahoo.portfolioapi.entity.Role;
import io.github.yubrajsahoo.portfolioapi.entity.User;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

class EntityTest {

    @Test
    void testPrivilege() {
        Privilege privilege = new Privilege();
        assertNull(privilege.getId());
        assertNull(privilege.getName());

        privilege.setId(1L);
        privilege.setName("READ");
        
        assertEquals(1L, privilege.getId());
        assertEquals("READ", privilege.getName());
    }

    @Test
    void testRole() {
        Role role = new Role();
        assertNull(role.getId());
        assertNull(role.getName());
        assertNull(role.getPrivileges());

        role.setId(1L);
        role.setName("USER");
        
        Privilege privilege = new Privilege();
        privilege.setName("READ");
        role.setPrivileges(List.of(privilege));
        
        assertEquals(1L, role.getId());
        assertEquals("USER", role.getName());
        assertEquals(1, role.getPrivileges().size());
    }

    @Test
    void testUser() {
        User user = new User();
        assertNull(user.getId());
        assertNull(user.getEmail());
        assertNull(user.getPassword());
        assertNull(user.getRoles());

        user.setId(1L);
        user.setEmail("test@test.com");
        user.setPassword("pass");
        
        Role role = new Role();
        role.setName("USER");
        user.setRoles(List.of(role));
        
        assertEquals(1L, user.getId());
        assertEquals("test@test.com", user.getEmail());
        assertEquals("pass", user.getPassword());
        assertEquals(1, user.getRoles().size());
    }

    @Test
    void testContactMessage() {
        io.github.yubrajsahoo.portfolioapi.entity.ContactMessage message = new io.github.yubrajsahoo.portfolioapi.entity.ContactMessage();
        assertNull(message.getId());
        assertNull(message.getName());
        assertNull(message.getEmail());
        assertNull(message.getMessage());
        org.junit.jupiter.api.Assertions.assertFalse(message.isRead());
        assertNull(message.getCreatedAt());

        message.setId(10L);
        message.setName("John");
        message.setEmail("john@example.com");
        message.setMessage("Hello");
        message.setRead(true);
        message.prePersist();

        assertEquals(10L, message.getId());
        assertEquals("John", message.getName());
        assertEquals("john@example.com", message.getEmail());
        assertEquals("Hello", message.getMessage());
        org.junit.jupiter.api.Assertions.assertTrue(message.isRead());
        org.junit.jupiter.api.Assertions.assertNotNull(message.getCreatedAt());

        io.github.yubrajsahoo.portfolioapi.entity.ContactMessage built = io.github.yubrajsahoo.portfolioapi.entity.ContactMessage.builder()
                .id(20L)
                .name("Alice")
                .email("alice@example.com")
                .message("Inquiry")
                .isRead(false)
                .build();
        assertEquals(20L, built.getId());
        assertEquals("Alice", built.getName());
        assertEquals("alice@example.com", built.getEmail());
        assertEquals("Inquiry", built.getMessage());
        org.junit.jupiter.api.Assertions.assertFalse(built.isRead());
    }
}
