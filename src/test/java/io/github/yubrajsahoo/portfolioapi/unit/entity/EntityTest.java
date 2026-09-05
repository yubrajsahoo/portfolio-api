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
}
