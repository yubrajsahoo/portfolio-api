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
 * Represents the metadata associated with a file, such as its name, extension,
 * folder location, access type, and resource type.
 * This domain class is used to encapsulate information needed for file storage and retrieval.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FileMetaData {

    /**
     * The name of the file without its extension.
     */
    private String fileName;

    /**
     * The extension of the file (e.g., png, pdf, mp4).
     */
    private String extension;

    /**
     * The folder path where the file is stored or should be stored.
     */
    private String folder;

    /**
     * The access type of the file (e.g., PUBLIC, PRIVATE), indicating its visibility.
     */
    private AccessType accessType = AccessType.PUBLIC;

    /**
     * The resource type of the file (e.g., IMAGE, VIDEO, RAW).
     */
    private ResourceType resourceType;
}
