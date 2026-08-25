/*
 *
 *  * Copyright (c) 2026 Yubraj Sahoo. All rights reserved.
 *
 */

package io.github.yubrajsahoo.portfolioapi.domain;

import io.github.yubrajsahoo.portfolioapi.enums.AccessType;
import io.github.yubrajsahoo.portfolioapi.enums.ResourceType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Domain class for file metadata
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FileMetaData {

    /**
     * represents file name
     */
    private String fileName;

    /**
     * represents file extension like png,pdf...
     */
    private String extension;

    /**
     * represent in which folder file should store
     */
    private String folder;

    /**
     * the access type of file
     */
    private AccessType accessType;

    /**
     * represents file resource type
     */
    private ResourceType resourceType;
}
