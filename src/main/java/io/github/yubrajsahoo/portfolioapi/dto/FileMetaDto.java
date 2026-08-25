/*
 * Copyright (c) 2026 Yubraj Sahoo. All rights reserved.
 */
package io.github.yubrajsahoo.portfolioapi.dto;

import io.github.yubrajsahoo.portfolioapi.enums.AccessType;
import lombok.Data;

/**
 * DTO for file metadata.
 *
 * @author Yubraj Sahoo
 */
@Data
public class FileMetaDto {

    /**
     * Stored file name
     */
    private String fileName;

    /**
     * Stored access type
     */
    private AccessType accessType;
}
