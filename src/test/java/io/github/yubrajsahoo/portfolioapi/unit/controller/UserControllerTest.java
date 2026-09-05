package io.github.yubrajsahoo.portfolioapi.unit.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.yubrajsahoo.portfolioapi.dto.PrivilegeReqDto;
import io.github.yubrajsahoo.portfolioapi.dto.PrivilegeResDto;
import io.github.yubrajsahoo.portfolioapi.dto.RoleReqDto;
import io.github.yubrajsahoo.portfolioapi.dto.RoleResDto;
import io.github.yubrajsahoo.portfolioapi.dto.UserReqDto;
import io.github.yubrajsahoo.portfolioapi.dto.UserResDto;
import io.github.yubrajsahoo.portfolioapi.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.springframework.security.test.context.support.WithMockUser;

@SpringBootTest
@DisplayName("Unit: User Controller Endpoints")
@ActiveProfiles("unit")
@WithMockUser(authorities = {"WRITE_PRIVILEGE", "READ_PRIVILEGE", "DELETE_PRIVILEGE"})
class UserControllerTest {

    @Autowired
    private WebApplicationContext context;

    private MockMvc mockMvc;

    @MockitoBean
    private UserService userService;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @BeforeEach
    void setup() {
        mockMvc = MockMvcBuilders.webAppContextSetup(context).build();
    }

    @Test
    @DisplayName("Should successfully register a new user")
    void registerUser() throws Exception {
        UserReqDto dto = new UserReqDto();
        dto.setEmail("test@test.com");
        dto.setPassword("password");
        dto.setRoles(List.of("USER"));

        when(userService.register(any(UserReqDto.class)))
                .thenReturn("User registered successfully with email id: test@test.com");

        mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk())
                .andExpect(content().string("User registered successfully with email id: test@test.com"));
    }

    @Test
    @DisplayName("Should fetch all users")
    void getAllUsers() throws Exception {
        UserResDto dto = new UserResDto();
        dto.setEmail("test@example.com");

        when(userService.getAllUsers()).thenReturn(List.of(dto));

        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/users"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$[0].email").value("test@example.com"));
    }

    @Test
    @DisplayName("Should create a new role")
    void createRole() throws Exception {
        RoleReqDto roleDto = new RoleReqDto();
        roleDto.setName("ADMIN");

        when(userService.insertRole(any(RoleReqDto.class)))
                .thenReturn("Role created successfully: ADMIN");

        mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/users/roles")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(roleDto)))
                .andExpect(status().isOk())
                .andExpect(content().string("Role created successfully: ADMIN"));
    }

    @Test
    @DisplayName("Should assign a role to a user")
    void assignRoleToUser() throws Exception {
        when(userService.assignRoleToUser("test@test.com", "ADMIN"))
                .thenReturn("Role assigned successfully");

        mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/users/test@test.com/roles/ADMIN"))
                .andExpect(status().isOk())
                .andExpect(content().string("Role assigned successfully"));
    }

    @Test
    @DisplayName("Should fetch all roles")
    void getAllRoles() throws Exception {
        RoleResDto role = new RoleResDto();
        role.setName("USER");

        when(userService.getAllRoles()).thenReturn(List.of(role));

        mockMvc.perform(get("/api/v1/users/roles")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$[0].name").value("USER"));

        verify(userService, times(1)).getAllRoles();
    }

    @Test
    @DisplayName("Should create a new privilege")
    void createPrivilege() throws Exception {
        PrivilegeReqDto dto = new PrivilegeReqDto();
        dto.setName("READ");

        when(userService.insertPrivilege(any(PrivilegeReqDto.class))).thenReturn("Privilege created successfully");

        mockMvc.perform(post("/api/v1/users/privileges")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk())
                .andExpect(content().string("Privilege created successfully"));

        verify(userService, times(1)).insertPrivilege(any(PrivilegeReqDto.class));
    }

    @Test
    @DisplayName("Should fetch all privileges")
    void getAllPrivileges() throws Exception {
        PrivilegeResDto priv = new PrivilegeResDto();
        priv.setName("READ");

        when(userService.getAllPrivileges()).thenReturn(List.of(priv));

        mockMvc.perform(get("/api/v1/users/privileges")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$[0].name").value("READ"));

        verify(userService, times(1)).getAllPrivileges();
    }

    @Test
    @DisplayName("Should assign a privilege to a role")
    void assignPrivilegeToRole() throws Exception {
        when(userService.assignPrivilegeToRole("ADMIN", "READ_PRIVILEGE"))
                .thenReturn("Privilege assigned successfully");

        mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/users/roles/ADMIN/privileges/READ_PRIVILEGE"))
                .andExpect(status().isOk())
                .andExpect(content().string("Privilege assigned successfully"));
    }
}
