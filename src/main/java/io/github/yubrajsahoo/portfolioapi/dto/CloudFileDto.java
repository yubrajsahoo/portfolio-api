/*
 *
 *  * Copyright (c) 2026 Yubraj Sahoo. All rights reserved.
 *
 */

package io.github.yubrajsahoo.portfolioapi.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.github.yubrajsahoo.portfolioapi.enums.AccessType;
import io.github.yubrajsahoo.portfolioapi.enums.ResourceType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.net.URI;

/**
 * Data Transfer Object (DTO) representing a file stored in the cloud.
 * Contains metadata and access information for the file.
 *
 * @author Yubraj Sahoo
 * @since 0.0.1-SNAPSHOT
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CloudFileDto {

    /**
     * The name of the file.
     */
    private String fileName;

    /**
     * The type of the resource (e.g., image, document, video).
     */
    private ResourceType type;

    /**
     * The access type of the file, determining who can access it (e.g., public, private).
     */
    private AccessType access;

    /**
     * The name of the project this file is associated with.
     */
    private String project;

    /**
     * The URL to access the cloud file.
     */
    private URI url;
}
