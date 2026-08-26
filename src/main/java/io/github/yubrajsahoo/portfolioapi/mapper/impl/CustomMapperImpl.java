/*
 *
 *  * Copyright (c) 2026 Yubraj Sahoo. All rights reserved.
 *
 */

package io.github.yubrajsahoo.portfolioapi.mapper.impl;

import io.github.yubrajsahoo.portfolioapi.domain.FileMetaData;
import io.github.yubrajsahoo.portfolioapi.enums.AccessType;
import io.github.yubrajsahoo.portfolioapi.enums.ResourceType;
import io.github.yubrajsahoo.portfolioapi.mapper.CustomMapper;
import io.github.yubrajsahoo.portfolioapi.utils.FileUtils;
import org.springframework.stereotype.Service;

/**
 * Implementation of the {@link CustomMapper} interface.
 * <p>
 * Provides concrete logic for mapping between DTOs and Domain models,
 * including necessary transformations such as extracting file extensions,
 * determining resource types, and building appropriate upload folders.
 * </p>
 *
 * @author Yubraj Sahoo
 * @since 0.0.1-SNAPSHOT
 */
@Service
public class CustomMapperImpl implements CustomMapper {

    /**
     * Converts a file name and access type to a {@link FileMetaData} domain object.
     * <p>
     * This implementation sanitizes the file name, extracts its extension,
     * determines the appropriate {@link ResourceType}, and constructs the
     * upload folder path based on the access and resource types.
     * </p>
     *
     * @param fileName   the name of the file
     * @param accessType the access type of the file
     * @return the fully constructed {@link FileMetaData} domain model
     */
    @Override
    public FileMetaData toFileMetaData(String fileName, AccessType accessType) {
        fileName = FileUtils.sanitizeFileName(fileName);
        FileUtils.assertFileName(fileName);

        String extension = FileUtils.getFileExtension(fileName);
        ResourceType resourceType = ResourceType.fromExtension(extension);

        return FileMetaData.builder()
                .fileName(FileUtils.removeFileExtension(fileName))
                .extension(extension)
                .folder(FileUtils.buildUploadFolder(accessType, resourceType))
                .accessType(accessType)
                .resourceType(resourceType)
                .build();
    }
}
