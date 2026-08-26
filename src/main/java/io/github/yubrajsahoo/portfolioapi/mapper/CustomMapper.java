/*
 *
 *  * Copyright (c) 2026 Yubraj Sahoo. All rights reserved.
 *
 */

package io.github.yubrajsahoo.portfolioapi.mapper;

import io.github.yubrajsahoo.portfolioapi.domain.FileMetaData;
import io.github.yubrajsahoo.portfolioapi.enums.AccessType;

/**
 * Interface defining custom mapping operations between Data Transfer Objects (DTOs)
 * and Domain models.
 *
 * @author Yubraj Sahoo
 * @since 0.0.1-SNAPSHOT
 */
public interface CustomMapper {
    /**
     * Converts a file name and access type to a {@link FileMetaData} domain object.
     *
     * @param fileName   the name of the file
     * @param accessType the access type of the file
     * @return the corresponding {@link FileMetaData} domain model
     */
    FileMetaData toFileMetaData(String fileName, AccessType accessType);
}
