package io.github.yubrajsahoo.portfolioapi.repository;

import io.github.yubrajsahoo.portfolioapi.entity.Privilege;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PrivilegeRepository extends JpaRepository<Privilege, Long> {
    List<Privilege> findByNameIn(List<String> names);
    boolean existsByName(String name);
}
