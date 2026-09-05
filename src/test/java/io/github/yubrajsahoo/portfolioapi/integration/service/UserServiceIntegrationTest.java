package io.github.yubrajsahoo.portfolioapi.integration.service;

import io.github.yubrajsahoo.portfolioapi.dto.PrivilegeReqDto;
import io.github.yubrajsahoo.portfolioapi.dto.RoleReqDto;
import io.github.yubrajsahoo.portfolioapi.dto.UserReqDto;
import io.github.yubrajsahoo.portfolioapi.entity.Privilege;
import io.github.yubrajsahoo.portfolioapi.entity.Role;
import io.github.yubrajsahoo.portfolioapi.entity.User;
import io.github.yubrajsahoo.portfolioapi.exception.RegistrationException;
import io.github.yubrajsahoo.portfolioapi.repository.PrivilegeRepository;
import io.github.yubrajsahoo.portfolioapi.repository.RoleRepository;
import io.github.yubrajsahoo.portfolioapi.repository.UserRepository;
import io.github.yubrajsahoo.portfolioapi.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@Slf4j
@SpringBootTest
@DisplayName("Integration: User Service Operations")
@ActiveProfiles("integration")
@Transactional
class UserServiceIntegrationTest {

    @Autowired
    private UserService userService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private PrivilegeRepository privilegeRepository;

    @AfterEach
    void tearDown() {
        userRepository.deleteAll();
        roleRepository.deleteAll();
        privilegeRepository.deleteAll();
    }

    @Test
    @DisplayName("Should successfully insert a Privilege")
    void testInsertPrivilege() {
        PrivilegeReqDto dto = new PrivilegeReqDto();
        dto.setName("READ_PRIVILEGE");

        String result = userService.insertPrivilege(dto);

        assertTrue(result.contains("Privilege created successfully"));
        assertTrue(privilegeRepository.existsByName("READ_PRIVILEGE"));
    }

    @Test
    @DisplayName("Should successfully insert a Role with existing Privilege")
    void testInsertRole() {
        Privilege priv = new Privilege();
        priv.setName("WRITE_PRIVILEGE");
        privilegeRepository.save(priv);

        RoleReqDto roleDto = new RoleReqDto();
        roleDto.setName("MANAGER");
        roleDto.setPrivileges(List.of("WRITE_PRIVILEGE"));

        String result = userService.insertRole(roleDto);

        assertTrue(result.contains("Role created successfully"));
        assertTrue(roleRepository.existsByName("MANAGER"));

        Role savedRole = roleRepository.findByNameIn(List.of("MANAGER")).getFirst();
        assertEquals(1, savedRole.getPrivileges().size());
        assertEquals("WRITE_PRIVILEGE", savedRole.getPrivileges().getFirst().getName());
    }

    @Test
    @DisplayName("Should successfully register a new User with existing Role")
    void testRegisterUser() {
        Role role = new Role();
        role.setName("USER");
        roleRepository.save(role);

        UserReqDto userDto = new UserReqDto();
        userDto.setEmail("newuser@example.com");
        userDto.setPassword("secretpassword");
        userDto.setRoles(List.of("USER"));

        String result = userService.register(userDto);

        assertTrue(result.contains("User registered successfully"));
        assertTrue(userRepository.existsByEmail("newuser@example.com"));

        User savedUser = userRepository.findByEmail("newuser@example.com").get();
        assertEquals("newuser@example.com", savedUser.getEmail());
        assertEquals(1, savedUser.getRoles().size());
        assertEquals("USER", savedUser.getRoles().getFirst().getName());
    }

    @Test
    @DisplayName("Should assign an existing Role to an existing User")
    void testAssignRoleToUser() {
        User user = new User();
        user.setEmail("assignuser@example.com");
        user.setPassword("password");
        userRepository.save(user);

        Role role = new Role();
        role.setName("ADMIN");
        roleRepository.save(role);

        String result = userService.assignRoleToUser("assignuser@example.com", "ADMIN");

        assertEquals("Role assigned successfully", result);

        User updatedUser = userRepository.findByEmail("assignuser@example.com").get();
        assertEquals(1, updatedUser.getRoles().size());
        assertEquals("ADMIN", updatedUser.getRoles().getFirst().getName());
    }

    @Test
    @DisplayName("Should assign an existing Privilege to an existing Role")
    void testAssignPrivilegeToRole() {
        Role role = new Role();
        role.setName("EDITOR");
        roleRepository.save(role);

        Privilege privilege = new Privilege();
        privilege.setName("EDIT_CONTENT");
        privilegeRepository.save(privilege);

        String result = userService.assignPrivilegeToRole("EDITOR", "EDIT_CONTENT");

        assertEquals("Privilege assigned successfully", result);

        Role updatedRole = roleRepository.findByNameIn(List.of("EDITOR")).getFirst();
        assertEquals(1, updatedRole.getPrivileges().size());
        assertEquals("EDIT_CONTENT", updatedRole.getPrivileges().getFirst().getName());
    }

    @Test
    @DisplayName("Should throw RegistrationException when registering an existing User")
    void testRegisterUser_AlreadyExists() {
        User user = new User();
        user.setEmail("duplicate@example.com");
        user.setPassword("pass");
        userRepository.save(user);

        UserReqDto userDto = new UserReqDto();
        userDto.setEmail("duplicate@example.com");
        userDto.setPassword("anotherpass");
        userDto.setRoles(List.of("USER"));

        assertThrows(RegistrationException.class, () -> userService.register(userDto));
    }
}
