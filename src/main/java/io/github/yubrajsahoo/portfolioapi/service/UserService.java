/*
 *
 *  * Copyright (c) 2026 Yubraj Sahoo. All rights reserved.
 *
 */

package io.github.yubrajsahoo.portfolioapi.service;

import io.github.yubrajsahoo.portfolioapi.dto.PrivilegeReqDto;
import io.github.yubrajsahoo.portfolioapi.dto.PrivilegeResDto;
import io.github.yubrajsahoo.portfolioapi.dto.RoleReqDto;
import io.github.yubrajsahoo.portfolioapi.dto.RoleResDto;
import io.github.yubrajsahoo.portfolioapi.dto.UserReqDto;
import io.github.yubrajsahoo.portfolioapi.dto.UserResDto;

import java.util.List;

/**
 * Service for managing users, roles, and privileges.
 */
public interface UserService {
    /**
     * Registers a new user.
     * @param userDto the user registration data
     * @return a success message
     */
    String register(UserReqDto userDto);

    /**
     * Retrieves all users.
     * @return a list of users
     */
    List<UserResDto> getAllUsers();

    /**
     * Creates a new role.
     * @param roleDto the role data
     * @return a success message
     */
    String insertRole(RoleReqDto roleDto);

    /**
     * Retrieves all roles.
     * @return a list of roles
     */
    List<RoleResDto> getAllRoles();

    /**
     * Creates a new privilege.
     * @param privilegeDto the privilege data
     * @return a success message
     */
    String insertPrivilege(PrivilegeReqDto privilegeDto);

    /**
     * Retrieves all privileges.
     * @return a list of privileges
     */
    List<PrivilegeResDto> getAllPrivileges();

    /**
     * Assigns a role to a user.
     * @param email the user email
     * @param roleName the role name
     * @return a success message
     */
    String assignRoleToUser(String email, String roleName);

    /**
     * Assigns a privilege to a role.
     * @param roleName the role name
     * @param privilegeName the privilege name
     * @return a success message
     */
    String assignPrivilegeToRole(String roleName, String privilegeName);
}
