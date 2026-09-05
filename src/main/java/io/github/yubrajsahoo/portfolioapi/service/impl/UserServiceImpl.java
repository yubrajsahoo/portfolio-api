/*
 *
 *  * Copyright (c) 2026 Yubraj Sahoo. All rights reserved.
 *
 */

package io.github.yubrajsahoo.portfolioapi.service.impl;

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
import io.github.yubrajsahoo.portfolioapi.metrics.MetricsType;
import io.github.yubrajsahoo.portfolioapi.repository.PrivilegeRepository;
import io.github.yubrajsahoo.portfolioapi.repository.RoleRepository;
import io.github.yubrajsahoo.portfolioapi.repository.UserRepository;
import io.github.yubrajsahoo.portfolioapi.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Implementation of the UserService for managing users, roles, and privileges.
 */
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PrivilegeRepository privilegeRepository;
    private final CustomMapper customMapper;
    private final PasswordEncoder passwordEncoder;

    /**
     * Registers a new user.
     *
     * @param userDto the user registration data
     * @return a success message
     * @throws RegistrationException if the user already exists or no valid roles found
     */
    @Override
    @Transactional
    public String register(UserReqDto userDto) {
        if (userRepository.existsByEmail(userDto.getEmail())) {
            throw new RegistrationException(
                    "User with email " + userDto.getEmail() + " already exists.",
                    MetricsType.FAILURE
            );
        }
        List<Role> roles = roleRepository.findByNameIn(userDto.getRoles());
        if (roles.isEmpty()) {
            throw new RegistrationException(
                    "No valid roles found for the user.",
                    MetricsType.FAILURE
            );
        }

        User user = customMapper.toUserEntity(userDto, roles);
        user.setPassword(passwordEncoder.encode(userDto.getPassword()));

        User savedUser = userRepository.save(user);

        return "User registered successfully with email id: " + savedUser.getEmail();
    }

    /**
     * Retrieves all users.
     *
     * @return a list of all user DTOs
     */
    @Override
    public List<UserResDto> getAllUsers() {
        return userRepository.findAll().stream().map(user -> {
            UserResDto dto = new UserResDto();
            dto.setEmail(user.getEmail());
            dto.setRoles(user.getRoles().stream().map(role -> {
                RoleResDto roleDto = new RoleResDto();
                roleDto.setName(role.getName());
                roleDto.setPrivileges(role.getPrivileges().stream().map(privilege -> {
                    PrivilegeResDto privDto = new PrivilegeResDto();
                    privDto.setName(privilege.getName());
                    return privDto;
                }).toList());
                return roleDto;
            }).toList());
            return dto;
        }).toList();
    }

    /**
     * Creates a new role.
     *
     * @param roleDto the role data
     * @return a success message
     * @throws RegistrationException if the role already exists
     */
    @Override
    @Transactional
    public String insertRole(RoleReqDto roleDto) {
        if (roleRepository.existsByName(roleDto.getName())) {
            throw new RegistrationException(
                    "Role with name " + roleDto.getName() + " already exists.",
                    MetricsType.FAILURE
            );
        }
        Role role = new Role();
        role.setName(roleDto.getName());
        if (roleDto.getPrivileges() != null && !roleDto.getPrivileges().isEmpty()) {
            role.setPrivileges(privilegeRepository.findByNameIn(roleDto.getPrivileges()));
        } else {
            role.setPrivileges(new ArrayList<>());
        }
        roleRepository.save(role);
        return "Role created successfully: " + role.getName();
    }

    /**
     * Retrieves all roles.
     *
     * @return a list of all role DTOs
     */
    @Override
    public List<RoleResDto> getAllRoles() {
        return roleRepository.findAll().stream().map(role -> {
            RoleResDto dto = new RoleResDto();
            dto.setName(role.getName());
            dto.setPrivileges(role.getPrivileges().stream().map(privilege -> {
                PrivilegeResDto privDto = new PrivilegeResDto();
                privDto.setName(privilege.getName());
                return privDto;
            }).toList());
            return dto;
        }).toList();
    }

    /**
     * Creates a new privilege.
     *
     * @param privilegeDto the privilege data
     * @return a success message
     * @throws RegistrationException if the privilege already exists
     */
    @Override
    @Transactional
    public String insertPrivilege(PrivilegeReqDto privilegeDto) {
        if (privilegeRepository.existsByName(privilegeDto.getName())) {
            throw new RegistrationException(
                    "Privilege with name " + privilegeDto.getName() + " already exists.",
                    MetricsType.FAILURE
            );
        }
        Privilege privilege = new Privilege();
        privilege.setName(privilegeDto.getName());
        privilegeRepository.save(privilege);
        return "Privilege created successfully: " + privilege.getName();
    }

    /**
     * Retrieves all privileges.
     *
     * @return a list of all privilege DTOs
     */
    @Override
    public List<PrivilegeResDto> getAllPrivileges() {
        return privilegeRepository.findAll().stream().map(privilege -> {
            PrivilegeResDto dto = new PrivilegeResDto();
            dto.setName(privilege.getName());
            return dto;
        }).toList();
    }

    /**
     * Assigns a role to a user.
     *
     * @param email    the email of the user
     * @param roleName the name of the role
     * @return a success message or not found message
     */
    @Override
    @Transactional
    public String assignRoleToUser(String email, String roleName) {
        Optional<User> userOpt = userRepository.findByEmail(email);
        if (userOpt.isEmpty()) return "User not found";

        List<Role> roles = roleRepository.findByNameIn(List.of(roleName));
        if (roles.isEmpty()) return "Role not found";

        User user = userOpt.get();
        if (user.getRoles() == null) {
            user.setRoles(new ArrayList<>());
        }
        if (!user.getRoles().contains(roles.getFirst())) {
            user.getRoles().add(roles.getFirst());
            userRepository.save(user);
        }
        return "Role assigned successfully";
    }

    /**
     * Assigns a privilege to a role.
     *
     * @param roleName      the name of the role
     * @param privilegeName the name of the privilege
     * @return a success message or not found message
     */
    @Override
    @Transactional
    public String assignPrivilegeToRole(String roleName, String privilegeName) {
        List<Role> roles = roleRepository.findByNameIn(List.of(roleName));
        if (roles.isEmpty()) return "Role not found";

        List<Privilege> privileges = privilegeRepository.findByNameIn(List.of(privilegeName));
        if (privileges.isEmpty()) return "Privilege not found";

        Role role = roles.getFirst();
        if (role.getPrivileges() == null) {
            role.setPrivileges(new ArrayList<>());
        }
        if (!role.getPrivileges().contains(privileges.getFirst())) {
            role.getPrivileges().add(privileges.getFirst());
            roleRepository.save(role);
        }
        return "Privilege assigned successfully";
    }
}
