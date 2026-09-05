/*
 *
 *  * Copyright (c) 2026 Yubraj Sahoo. All rights reserved.
 *
 */

package io.github.yubrajsahoo.portfolioapi.controller;

import io.github.yubrajsahoo.portfolioapi.dto.PrivilegeReqDto;
import io.github.yubrajsahoo.portfolioapi.dto.PrivilegeResDto;
import io.github.yubrajsahoo.portfolioapi.dto.RoleReqDto;
import io.github.yubrajsahoo.portfolioapi.dto.RoleResDto;
import io.github.yubrajsahoo.portfolioapi.dto.UserReqDto;
import io.github.yubrajsahoo.portfolioapi.dto.UserResDto;
import io.github.yubrajsahoo.portfolioapi.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * Controller for managing users, roles, and privileges.
 * Provides endpoints for creating and retrieving users, roles, privileges,
 * and assigning them to each other.
 *
 * @author Yubraj Sahoo
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/users")
public class UserController {
    private final UserService userService;

    /**
     * Registers a new user.
     *
     * @param userDto the user registration data
     * @return a success message
     */
    @PostMapping
    @PreAuthorize("hasAuthority('WRITE_PRIVILEGE')")
    public ResponseEntity<String> registerUser(@RequestBody UserReqDto userDto) {
        return ResponseEntity.ok(userService.register(userDto));
    }

    /**
     * Retrieves a list of all registered users.
     *
     * @return a list of user details
     */
    @GetMapping
    @PreAuthorize("hasAuthority('READ_PRIVILEGE')")
    public ResponseEntity<List<UserResDto>> getAllUsers() {
        return ResponseEntity.ok(userService.getAllUsers());
    }

    /**
     * Creates a new role.
     *
     * @param roleDto the role details
     * @return a success message
     */
    @PostMapping("/roles")
    @PreAuthorize("hasAuthority('WRITE_PRIVILEGE')")
    public ResponseEntity<String> createRole(@RequestBody RoleReqDto roleDto) {
        return ResponseEntity.ok(userService.insertRole(roleDto));
    }

    /**
     * Retrieves all roles in the system.
     *
     * @return a list of roles with their privileges
     */
    @GetMapping("/roles")
    @PreAuthorize("hasAuthority('READ_PRIVILEGE')")
    public ResponseEntity<List<RoleResDto>> getAllRoles() {
        return ResponseEntity.ok(userService.getAllRoles());
    }

    /**
     * Creates a new privilege.
     *
     * @param privilegeDto the privilege details
     * @return a success message
     */
    @PostMapping("/privileges")
    @PreAuthorize("hasAuthority('WRITE_PRIVILEGE')")
    public ResponseEntity<String> createPrivilege(@RequestBody PrivilegeReqDto privilegeDto) {
        return ResponseEntity.ok(userService.insertPrivilege(privilegeDto));
    }

    /**
     * Retrieves all privileges in the system.
     *
     * @return a list of privileges
     */
    @GetMapping("/privileges")
    @PreAuthorize("hasAuthority('READ_PRIVILEGE')")
    public ResponseEntity<List<PrivilegeResDto>> getAllPrivileges() {
        return ResponseEntity.ok(userService.getAllPrivileges());
    }

    /**
     * Assigns a given role to a user.
     *
     * @param email    the email of the user
     * @param roleName the name of the role to assign
     * @return a success message
     */
    @PostMapping("/{email}/roles/{roleName}")
    @PreAuthorize("hasAuthority('WRITE_PRIVILEGE')")
    public ResponseEntity<String> assignRoleToUser(@PathVariable String email, @PathVariable String roleName) {
        return ResponseEntity.ok(userService.assignRoleToUser(email, roleName));
    }

    /**
     * Assigns a given privilege to a role.
     *
     * @param roleName      the name of the role
     * @param privilegeName the name of the privilege to assign
     * @return a success message
     */
    @PostMapping("/roles/{roleName}/privileges/{privilegeName}")
    @PreAuthorize("hasAuthority('WRITE_PRIVILEGE')")
    public ResponseEntity<String> assignPrivilegeToRole(@PathVariable String roleName, @PathVariable String privilegeName) {
        return ResponseEntity.ok(userService.assignPrivilegeToRole(roleName, privilegeName));
    }
}
