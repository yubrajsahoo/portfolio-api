/*
 *
 *  * Copyright (c) 2026 Yubraj Sahoo. All rights reserved.
 *
 */

package io.github.yubrajsahoo.portfolioapi.mapper.impl;

import io.github.yubrajsahoo.portfolioapi.constants.CloudinaryConstants;
import io.github.yubrajsahoo.portfolioapi.domain.FileMetaData;
import io.github.yubrajsahoo.portfolioapi.dto.CloudFileDto;
import io.github.yubrajsahoo.portfolioapi.dto.UserReqDto;
import io.github.yubrajsahoo.portfolioapi.entity.Role;
import io.github.yubrajsahoo.portfolioapi.entity.User;
import io.github.yubrajsahoo.portfolioapi.enums.AccessType;
import io.github.yubrajsahoo.portfolioapi.enums.ResourceType;
import io.github.yubrajsahoo.portfolioapi.mapper.CustomMapper;
import io.github.yubrajsahoo.portfolioapi.utils.FileUtils;
import org.springframework.stereotype.Service;

import java.net.URI;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Implementation of the {@link CustomMapper} interface.
 * <p>
 * Provides concrete logic for mapping between DTOs and Domain models,
 * including necessary transformations such as extracting file extensions,
 * determining resource types, and setting application names.
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
     * determines the appropriate {@link ResourceType}, and sets the
     * application name based on the configuration.
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
                .accessType(accessType)
                .resourceType(resourceType)
                .build();
    }

    /**
     * Converts a URL string to a {@link CloudFileDto} object.
     *
     * @param url the URL string of the cloud file
     * @return the corresponding {@link CloudFileDto} object containing the file URL
     */
    @Override
    public CloudFileDto toCloudFileDto(String url) {
        Pattern pattern = Pattern.compile(CloudinaryConstants.REGEX_CLOUDINARY_ULR);
        Matcher matcher = pattern.matcher(url);

        if (matcher.find()) {
            return CloudFileDto.builder()
                    .fileName(matcher.group(4))
                    .type(ResourceType.fromCloudinary(matcher.group(3)))
                    .access(AccessType.fromCloudinary(matcher.group((2))))
                    .application(matcher.group(1))
                    .url(URI.create(url))
                    .build();
        }
        return null;
    }

    @Override
    public User toUserEntity(UserReqDto userDto, List<Role> roles) {
        User user = new User();

        user.setEmail(userDto.getEmail());
        user.setPassword(userDto.getPassword());
        user.setRoles(roles);

        return user;
    }
}
