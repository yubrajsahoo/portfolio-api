/*
 *
 *  * Copyright (c) 2026 Yubraj Sahoo. All rights reserved.
 *
 */

package io.github.yubrajsahoo.portfolioapi.controller;

import io.github.yubrajsahoo.portfolioapi.dto.FileMetaDto;
import io.github.yubrajsahoo.portfolioapi.service.CloudFileService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.net.URI;

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
@RestController
@RequestMapping("/api/v1/files")
@RequiredArgsConstructor
@Tag(
        name = "Cloud Files",
        description = "APIs for uploading, accessing and deleting cloud files"
)
public class CloudFileController {
    private final CloudFileService cloudFileService;

    /**
     * Uploads a file to the configured cloud storage provider.
     *
     * @param file    the file to upload
     * @param metaDto the metadata associated with the file
     * @return a {@link ResponseEntity} containing the URL of the uploaded file
     */
    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @Operation(
            summary = "Upload a file",
            description = "Uploads a file to the configured cloud storage provider"
    )
    public ResponseEntity<String> upload(
            @RequestParam("file") MultipartFile file,
            @RequestBody FileMetaDto metaDto) {

        String uploadUrl = cloudFileService.upload(file, metaDto);

        return ResponseEntity
                .created(URI.create(uploadUrl))
                .body(uploadUrl);
    }

    /**
     * Retrieves the URL of a stored file.
     *
     * @param metaDto the metadata used to locate the file
     * @return a {@link ResponseEntity} containing the URI of the requested file
     */
    @Operation(
            summary = "Get file URL",
            description = "Returns the URL of a stored file"
    )
    @GetMapping
    public ResponseEntity<URI> download(@RequestBody FileMetaDto metaDto) {
        return ResponseEntity.ok(
                URI.create(
                        cloudFileService.getUrl(metaDto)
                )
        );
    }

    /**
     * Deletes a file from the configured cloud storage provider.
     *
     * @param metaDto the metadata used to locate and delete the file
     */
    @Operation(
            summary = "Delete a file",
            description = "Deletes a file from the configured cloud storage provider"
    )
    @DeleteMapping
    @ResponseStatus(HttpStatus.ACCEPTED)
    public void delete(FileMetaDto metaDto) {

        cloudFileService.delete(metaDto);
    }
}
