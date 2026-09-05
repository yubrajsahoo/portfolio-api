package io.github.yubrajsahoo.portfolioapi.unit.service;

import io.github.yubrajsahoo.portfolioapi.entity.Privilege;
import io.github.yubrajsahoo.portfolioapi.entity.Role;
import io.github.yubrajsahoo.portfolioapi.entity.User;
import io.github.yubrajsahoo.portfolioapi.repository.UserRepository;
import io.github.yubrajsahoo.portfolioapi.service.impl.CustomUserDetailsService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@SpringBootTest
@ActiveProfiles("unit")
@DisplayName("Unit: Custom User Details Service")
class CustomUserDetailsServiceTest {

    @MockitoBean
    private UserRepository userRepository;

    @Autowired
    private CustomUserDetailsService customUserDetailsService;

    @Test
    @DisplayName("Should Load User by Username Successfully")
    void loadUserByUsername_Success() {
        User user = new User();
        user.setEmail("test@test.com");
        user.setPassword("password");

        Privilege priv = new Privilege();
        priv.setName("READ");

        Role role = new Role();
        role.setName("USER");
        role.setPrivileges(List.of(priv));

        user.setRoles(List.of(role));

        when(userRepository.findByEmail("test@test.com")).thenReturn(Optional.of(user));

        UserDetails userDetails = customUserDetailsService.loadUserByUsername("test@test.com");

        assertNotNull(userDetails);
        assertEquals("test@test.com", userDetails.getUsername());
        assertEquals("password", userDetails.getPassword());
        assertEquals(1, userDetails.getAuthorities().size());
        assertTrue(userDetails.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("READ")));
    }

    @Test
    @DisplayName("Should Throw Exception when User Not Found")
    void loadUserByUsername_NotFound() {
        when(userRepository.findByEmail("test@test.com")).thenReturn(Optional.empty());

        assertThrows(UsernameNotFoundException.class, () -> customUserDetailsService.loadUserByUsername("test@test.com"));
    }
}
