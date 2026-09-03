package io.github.yubrajsahoo.portfolioapi.unit.service;

import io.github.yubrajsahoo.portfolioapi.dto.PrivilegeReqDto;
import io.github.yubrajsahoo.portfolioapi.dto.PrivilegeResDto;
import io.github.yubrajsahoo.portfolioapi.dto.RoleReqDto;
import io.github.yubrajsahoo.portfolioapi.dto.RoleResDto;
import io.github.yubrajsahoo.portfolioapi.dto.UserReqDto;
import io.github.yubrajsahoo.portfolioapi.dto.UserResDto;
import io.github.yubrajsahoo.portfolioapi.entity.Privilege;
import io.github.yubrajsahoo.portfolioapi.entity.Role;
import io.github.yubrajsahoo.portfolioapi.entity.User;
import io.github.yubrajsahoo.portfolioapi.exception.RegistrationException;
import io.github.yubrajsahoo.portfolioapi.mapper.CustomMapper;
import io.github.yubrajsahoo.portfolioapi.repository.PrivilegeRepository;
import io.github.yubrajsahoo.portfolioapi.repository.RoleRepository;
import io.github.yubrajsahoo.portfolioapi.repository.UserRepository;
import io.github.yubrajsahoo.portfolioapi.service.UserService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@SpringBootTest
@ActiveProfiles("unit")
@DisplayName("Unit: User Service Operations")
class UserServiceTest {

    @MockitoBean
    private UserRepository userRepository;

    @MockitoBean
    private RoleRepository roleRepository;

    @MockitoBean
    private PrivilegeRepository privilegeRepository;

    @MockitoBean
    private CustomMapper customMapper;

    @MockitoBean
    private PasswordEncoder passwordEncoder;

    @Autowired
    private UserService userService;

    @Test
    @DisplayName("Should Successfully Register a User")
    void registerUser_Success() {
        UserReqDto dto = new UserReqDto();
        dto.setEmail("test@test.com");
        dto.setPassword("pass");
        dto.setRoles(List.of("ADMIN"));

        Role role = new Role();
        role.setName("ADMIN");

        User user = new User();
        user.setEmail("test@test.com");

        when(userRepository.existsByEmail("test@test.com")).thenReturn(false);
        when(roleRepository.findByNameIn(List.of("ADMIN"))).thenReturn(List.of(role));
        when(customMapper.toUserEntity(dto, List.of(role))).thenReturn(user);
        when(passwordEncoder.encode("pass")).thenReturn("encoded_pass");
        when(userRepository.save(any(User.class))).thenReturn(user);

        String result = userService.register(dto);
        assertTrue(result.contains("User registered successfully"));
        verify(userRepository, times(1)).save(any(User.class));
    }

    @Test
    @DisplayName("Should Throw Exception when Registering Existing User")
    void registerUser_ThrowsExceptionWhenUserExists() {
        UserReqDto dto = new UserReqDto();
        dto.setEmail("test@test.com");

        when(userRepository.existsByEmail("test@test.com")).thenReturn(true);

        assertThrows(RegistrationException.class, () -> userService.register(dto));
    }

    @Test
    @DisplayName("Should Insert Role")
    void insertRole_Success() {
        RoleReqDto roleDto = new RoleReqDto();
        roleDto.setName("USER");
        roleDto.setPrivileges(List.of("READ"));

        Privilege priv = new Privilege();
        priv.setName("READ");

        when(roleRepository.existsByName("USER")).thenReturn(false);
        when(privilegeRepository.findByNameIn(List.of("READ"))).thenReturn(List.of(priv));

        String result = userService.insertRole(roleDto);
        assertTrue(result.contains("Role created successfully: USER"));
        verify(roleRepository, times(1)).save(any(Role.class));
    }

    @Test
    @DisplayName("Should Fetch All Users")
    void getAllUsers_Success() {
        User user = new User();
        user.setEmail("test@test.com");
        user.setRoles(new ArrayList<>());
        when(userRepository.findAll()).thenReturn(List.of(user));

        List<UserResDto> users = userService.getAllUsers();
        assertEquals(1, users.size());
        assertEquals("test@test.com", users.getFirst().getEmail());
    }

    @Test
    @DisplayName("Should Assign Role to User")
    void assignRoleToUser_Success() {
        User user = new User();
        user.setEmail("test@test.com");
        user.setRoles(new ArrayList<>());

        Role role = new Role();
        role.setName("ADMIN");

        when(userRepository.findByEmail("test@test.com")).thenReturn(Optional.of(user));
        when(roleRepository.findByNameIn(List.of("ADMIN"))).thenReturn(List.of(role));

        String result = userService.assignRoleToUser("test@test.com", "ADMIN");
        assertEquals("Role assigned successfully", result);
        verify(userRepository, times(1)).save(user);
    }

    @Test
    @DisplayName("Should Handle Assign Role when User Not Found")
    void assignRoleToUser_UserNotFound() {
        when(userRepository.findByEmail("test@test.com")).thenReturn(Optional.empty());

        String result = userService.assignRoleToUser("test@test.com", "ADMIN");
        assertEquals("User not found", result);
    }

    @Test
    @DisplayName("Should Handle Assign Role when Role Not Found")
    void assignRoleToUser_RoleNotFound() {
        User user = new User();
        user.setEmail("test@test.com");

        when(userRepository.findByEmail("test@test.com")).thenReturn(Optional.of(user));
        when(roleRepository.findByNameIn(List.of("ADMIN"))).thenReturn(new ArrayList<>());

        String result = userService.assignRoleToUser("test@test.com", "ADMIN");
        assertEquals("Role not found", result);
    }

    @Test
    @DisplayName("Should Throw Exception when Registering with Invalid Roles")
    void registerUser_ThrowsExceptionWhenRolesInvalid() {
        UserReqDto dto = new UserReqDto();
        dto.setEmail("test@test.com");
        dto.setRoles(List.of("INVALID_ROLE"));

        when(userRepository.existsByEmail("test@test.com")).thenReturn(false);
        when(roleRepository.findByNameIn(List.of("INVALID_ROLE"))).thenReturn(new ArrayList<>());

        assertThrows(RegistrationException.class, () -> userService.register(dto));
    }

    @Test
    @DisplayName("Should Throw Exception when Inserting Existing Role")
    void insertRole_ThrowsExceptionWhenRoleExists() {
        RoleReqDto roleDto = new RoleReqDto();
        roleDto.setName("USER");

        when(roleRepository.existsByName("USER")).thenReturn(true);

        assertThrows(RegistrationException.class, () -> userService.insertRole(roleDto));
    }
    
    @Test
    @DisplayName("Should Insert Role Without Privileges")
    void insertRole_WithoutPrivileges() {
        RoleReqDto roleDto = new RoleReqDto();
        roleDto.setName("USER");
        
        when(roleRepository.existsByName("USER")).thenReturn(false);
        
        String result = userService.insertRole(roleDto);
        assertTrue(result.contains("Role created successfully: USER"));
        verify(roleRepository, times(1)).save(any(Role.class));
    }

    @Test
    @DisplayName("Should Fetch All Roles")
    void getAllRoles_Success() {
        Role role = new Role();
        role.setName("ADMIN");
        role.setPrivileges(new ArrayList<>());
        when(roleRepository.findAll()).thenReturn(List.of(role));

        List<RoleResDto> roles = userService.getAllRoles();
        assertEquals(1, roles.size());
        assertEquals("ADMIN", roles.getFirst().getName());
    }

    @Test
    @DisplayName("Should Insert Privilege")
    void insertPrivilege_Success() {
        PrivilegeReqDto dto = new PrivilegeReqDto();
        dto.setName("READ");

        when(privilegeRepository.existsByName("READ")).thenReturn(false);

        String result = userService.insertPrivilege(dto);
        assertTrue(result.contains("Privilege created successfully: READ"));
        verify(privilegeRepository, times(1)).save(any(Privilege.class));
    }

    @Test
    @DisplayName("Should Throw Exception when Inserting Existing Privilege")
    void insertPrivilege_ThrowsExceptionWhenPrivilegeExists() {
        PrivilegeReqDto dto = new PrivilegeReqDto();
        dto.setName("READ");

        when(privilegeRepository.existsByName("READ")).thenReturn(true);

        assertThrows(RegistrationException.class, () -> userService.insertPrivilege(dto));
    }

    @Test
    @DisplayName("Should Fetch All Privileges")
    void getAllPrivileges_Success() {
        Privilege priv = new Privilege();
        priv.setName("READ");
        when(privilegeRepository.findAll()).thenReturn(List.of(priv));

        List<PrivilegeResDto> privileges = userService.getAllPrivileges();
        assertEquals(1, privileges.size());
        assertEquals("READ", privileges.getFirst().getName());
    }

    @Test
    @DisplayName("Should Assign Privilege to Role")
    void assignPrivilegeToRole_Success() {
        Role role = new Role();
        role.setName("ADMIN");
        role.setPrivileges(new ArrayList<>());

        Privilege priv = new Privilege();
        priv.setName("READ");

        when(roleRepository.findByNameIn(List.of("ADMIN"))).thenReturn(List.of(role));
        when(privilegeRepository.findByNameIn(List.of("READ"))).thenReturn(List.of(priv));

        String result = userService.assignPrivilegeToRole("ADMIN", "READ");
        assertEquals("Privilege assigned successfully", result);
        verify(roleRepository, times(1)).save(role);
    }
    
    @Test
    @DisplayName("Should Handle Assign Privilege when Role Not Found")
    void assignPrivilegeToRole_RoleNotFound() {
        when(roleRepository.findByNameIn(List.of("ADMIN"))).thenReturn(new ArrayList<>());

        String result = userService.assignPrivilegeToRole("ADMIN", "READ");
        assertEquals("Role not found", result);
    }
    
    @Test
    @DisplayName("Should Handle Assign Privilege when Privilege Not Found")
    void assignPrivilegeToRole_PrivilegeNotFound() {
        Role role = new Role();
        role.setName("ADMIN");
        
        when(roleRepository.findByNameIn(List.of("ADMIN"))).thenReturn(List.of(role));
        when(privilegeRepository.findByNameIn(List.of("READ"))).thenReturn(new ArrayList<>());

        String result = userService.assignPrivilegeToRole("ADMIN", "READ");
        assertEquals("Privilege not found", result);
    }
}
