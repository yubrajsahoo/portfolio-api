/*
 *
 *  * Copyright (c) 2026 Yubraj Sahoo. All rights reserved.
 *
 */

package io.github.yubrajsahoo.portfolioapi.controller;

import io.github.yubrajsahoo.portfolioapi.dto.ContactMessageDto;
import io.github.yubrajsahoo.portfolioapi.dto.ContactReqDto;
import io.github.yubrajsahoo.portfolioapi.dto.ContactResDto;
import io.github.yubrajsahoo.portfolioapi.service.ContactService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * REST controller for contact form submissions.
 * <p>
 * This controller provides endpoints for public visitors to submit messages
 * via the portfolio contact form, as well as an authenticated endpoint for
 * viewing received messages.
 * </p>
 *
 * @author Yubraj Sahoo
 */
@Slf4j
@RestController
@RequestMapping("/api/v1/contact")
@RequiredArgsConstructor
@Validated
@Tag(
        name = "Contact",
        description = "APIs for submitting and managing contact form messages"
)
public class ContactController {

    private final ContactService contactService;

    /**
     * Handles submission of contact form messages.
     * Publicly accessible for website visitors.
     *
     * @param contactReqDto the contact form payload
     * @return a {@link ResponseEntity} with status 201 Created and the confirmation message
     */
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @org.amyth.core.annotation.RateLimit(permits = 3, window = 60, key = "'contact:' + (#request.getHeader('X-Forwarded-For') ?: #request.remoteAddr)")
    @Operation(
            summary = "Submit a contact message",
            description = "Receives and saves a message from the portfolio contact form"
    )
    public ResponseEntity<ContactResDto> submitContactMessage(
            @Valid @RequestBody ContactReqDto contactReqDto
    ) {
        log.info("Contact form submitted by: {}", contactReqDto.getName());
        ContactResDto response = contactService.submitContactMessage(contactReqDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    /**
     * Retrieves all submitted contact messages.
     * Requires READ_PRIVILEGE authority.
     *
     * @return a list of all contact messages
     */
    @GetMapping
    @PreAuthorize("hasAuthority('READ_PRIVILEGE')")
    @Operation(
            summary = "Get all contact messages",
            description = "Returns a list of all submitted contact messages"
    )
    public ResponseEntity<List<ContactMessageDto>> getAllContactMessages() {
        return ResponseEntity.ok(contactService.getAllContactMessages());
    }
}
