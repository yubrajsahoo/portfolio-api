/*
 *
 *  * Copyright (c) 2026 Yubraj Sahoo. All rights reserved.
 *
 */

package io.github.yubrajsahoo.portfolioapi.repository;

import io.github.yubrajsahoo.portfolioapi.entity.ContactMessage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Repository interface for {@link ContactMessage} entities.
 *
 * @author Yubraj Sahoo
 */
@Repository
public interface ContactMessageRepository extends JpaRepository<ContactMessage, Long> {

    /**
     * Retrieves all contact messages ordered by creation date descending.
     *
     * @return a list of contact messages
     */
    List<ContactMessage> findAllByOrderByCreatedAtDesc();

    /**
     * Retrieves all contact messages for a specific email ordered by creation date descending.
     *
     * @param email the sender email
     * @return a list of matching contact messages
     */
    List<ContactMessage> findByEmailOrderByCreatedAtDesc(String email);
}
