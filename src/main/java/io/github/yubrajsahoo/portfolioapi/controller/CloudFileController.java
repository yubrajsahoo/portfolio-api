/*
 *
 *  * Copyright (c) 2026 Yubraj Sahoo. All rights reserved.
 *
 */

package io.github.yubrajsahoo.portfolioapi.controller;

import io.github.yubrajsahoo.portfolioapi.dto.CloudFileDto;
import io.github.yubrajsahoo.portfolioapi.enums.AccessType;
import io.github.yubrajsahoo.portfolioapi.service.CloudFileService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.net.URI;
import java.util.List;

/**
 * REST controller for managing cloud files.
 * <p>
 * This controller provides endpoints for uploading files to cloud storage,
 * retrieving file URLs, and deleting files from the cloud.
 * </p>
 *
 * @author Yubraj Sahoo
 * @since 0.0.1-SNAPSHOT
 */
@Slf4j
@RestController
@RequestMapping("/api/v1/files")
@RequiredArgsConstructor
@Validated
@Tag(
        name = "Cloud Files",
        description = "APIs for uploading, accessing and deleting cloud files"
)
public class CloudFileController {
    private final CloudFileService cloudFileService;

    /**
     * Uploads a file to the configured cloud storage provider.
     *
     * @param file   the file to upload
     * @param access the access type of the file
     * @return a {@link ResponseEntity} containing the URL of the uploaded file
     */
    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @Operation(
            summary = "Upload a file",
            description = "Uploads a file to the configured cloud storage provider"
    )
    @PreAuthorize("hasAuthority('WRITE_PRIVILEGE')")
    public ResponseEntity<String> upload(
            @NotNull(message = "File cannot be null") @RequestParam MultipartFile file,
            @RequestParam(required = false, defaultValue = "PUBLIC") AccessType access
    ) {
        String uploadUrl = cloudFileService.upload(file, access);

        return ResponseEntity
                .created(URI.create(uploadUrl))
                .body(uploadUrl);
    }

    /**
     * Retrieves the URL of a stored file.
     *
     * @param fileName the name of the file
     * @param access   the access type of the file
     * @return a {@link ResponseEntity} containing the URI of the requested file
     */
    @Operation(
            summary = "Get file URL",
            description = "Returns the URL of a stored file"
    )
    @GetMapping("/{fileName}")
    public ResponseEntity<URI> get(
            @NotBlank(message = "File name cannot be blank") @PathVariable("fileName") String fileName,
            @RequestParam(required = false, defaultValue = "PUBLIC") AccessType access
    ) {
        return ResponseEntity.ok(
                URI.create(
                        cloudFileService.getUrl(fileName, access)
                )
        );
    }

    /**
     * Deletes a file from the configured cloud storage provider.
     *
     * @param fileName the name of the file
     * @param access   the access type of the file
     */
    @Operation(
            summary = "Delete a file",
            description = "Deletes a file from the configured cloud storage provider"
    )
    @DeleteMapping("/{fileName}")
    @ResponseStatus(HttpStatus.ACCEPTED)
    @PreAuthorize("hasAuthority('DELETE_PRIVILEGE')")
    public void delete(
            @NotBlank(message = "File name cannot be blank") @PathVariable String fileName,
            @RequestParam(required = false, defaultValue = "PUBLIC") AccessType access
    ) {
        cloudFileService.delete(fileName, access);
    }

    /**
     * Retrieves all file names for files uploaded.
     *
     * @return a {@link ResponseEntity} containing a list of file details
     */
    @Operation(
            summary = "Get all file names",
            description = "Returns a list of all file names for files uploaded in the portfolio folder"
    )
    @GetMapping
    @PreAuthorize("hasAuthority('READ_PRIVILEGE')")
    public ResponseEntity<List<CloudFileDto>> getAllFileNames(
            @RequestParam(required = false, defaultValue = "PUBLIC") AccessType access
    ) {
        return ResponseEntity.ok(cloudFileService.getAllFileNames(access));
    }
}
