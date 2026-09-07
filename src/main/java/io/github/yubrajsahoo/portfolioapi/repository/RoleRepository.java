/*
 *
 *  * Copyright (c) 2026 Yubraj Sahoo. All rights reserved.
 *
 */

package io.github.yubrajsahoo.portfolioapi.repository;

import io.github.yubrajsahoo.portfolioapi.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * Repository interface for managing Role entities.
 */
public interface RoleRepository extends JpaRepository<Role, Long> {

    /**
     * Finds roles by a list of names.
     *
     * @param names the list of role names
     * @return a list of roles
     */
    List<Role> findByNameIn(List<String> names);

    /**
     * Checks if a role exists by name.
     *
     * @param name the name of the role
     * @return true if the role exists, false otherwise
     */
    boolean existsByName(String name);
}
