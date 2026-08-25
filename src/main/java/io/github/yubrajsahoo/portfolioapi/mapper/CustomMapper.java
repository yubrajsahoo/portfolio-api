/*
 *
 *  * Copyright (c) 2026 Yubraj Sahoo. All rights reserved.
 *
 */

package io.github.yubrajsahoo.portfolioapi.mapper;

import io.github.yubrajsahoo.portfolioapi.domain.FileMetaData;
import io.github.yubrajsahoo.portfolioapi.dto.FileMetaDto;

/**
 * Interface defining custom mapping operations between Data Transfer Objects (DTOs)
 * and Domain models.
 *
 * @author Yubraj Sahoo
 * @since 0.0.1-SNAPSHOT
 */
public interface CustomMapper {

    /**
     * Converts a {@link FileMetaDto} to a {@link FileMetaData} domain object.
     *
     * @param fileMetaDto the data transfer object containing file metadata information
     * @return the corresponding {@link FileMetaData} domain model
     */
    FileMetaData toFileMetaData(FileMetaDto fileMetaDto);
}
