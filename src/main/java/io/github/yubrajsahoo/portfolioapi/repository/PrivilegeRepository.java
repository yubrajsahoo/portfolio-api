package io.github.yubrajsahoo.portfolioapi.repository;

import io.github.yubrajsahoo.portfolioapi.entity.Privilege;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * Repository interface for managing Privilege entities.
 */
public interface PrivilegeRepository extends JpaRepository<Privilege, Long> {
    /**
     * Finds privileges by a list of names.
     *
     * @param names the list of privilege names
     * @return a list of privileges
     */
    List<Privilege> findByNameIn(List<String> names);

    /**
     * Checks if a privilege exists by name.
     *
     * @param name the name of the privilege
     * @return true if the privilege exists, false otherwise
     */
    boolean existsByName(String name);
}
