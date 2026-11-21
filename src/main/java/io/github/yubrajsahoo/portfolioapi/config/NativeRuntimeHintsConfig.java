/*
 *
 *  * Copyright (c) 2026 Yubraj Sahoo. All rights reserved.
 *
 */

package io.github.yubrajsahoo.portfolioapi.config;

import io.github.yubrajsahoo.portfolioapi.domain.FileMetaData;
import io.github.yubrajsahoo.portfolioapi.dto.CloudFileDto;
import io.github.yubrajsahoo.portfolioapi.dto.ContactMessageDto;
import io.github.yubrajsahoo.portfolioapi.dto.ContactReqDto;
import io.github.yubrajsahoo.portfolioapi.dto.ContactResDto;
import io.github.yubrajsahoo.portfolioapi.dto.PrivilegeReqDto;
import io.github.yubrajsahoo.portfolioapi.dto.PrivilegeResDto;
import io.github.yubrajsahoo.portfolioapi.dto.RoleReqDto;
import io.github.yubrajsahoo.portfolioapi.dto.RoleResDto;
import io.github.yubrajsahoo.portfolioapi.dto.UserReqDto;
import io.github.yubrajsahoo.portfolioapi.dto.UserResDto;
import io.github.yubrajsahoo.portfolioapi.entity.ContactMessage;
import io.github.yubrajsahoo.portfolioapi.entity.Privilege;
import io.github.yubrajsahoo.portfolioapi.entity.Role;
import io.github.yubrajsahoo.portfolioapi.entity.User;
import org.springframework.aot.hint.annotation.RegisterReflectionForBinding;
import org.springframework.context.annotation.Configuration;

/**
 * Native runtime reflection registration to ensure all domain models,
 * DTOs, and entities serialize and deserialize seamlessly in GraalVM Native Image.
 */
@Configuration(proxyBeanMethods = false)
@RegisterReflectionForBinding({
        ContactReqDto.class,
        ContactResDto.class,
        ContactMessageDto.class,
        CloudFileDto.class,
        FileMetaData.class,
        UserReqDto.class,
        UserResDto.class,
        RoleReqDto.class,
        RoleResDto.class,
        PrivilegeReqDto.class,
        PrivilegeResDto.class,
        ContactMessage.class,
        User.class,
        Role.class,
        Privilege.class
})
public class NativeRuntimeHintsConfig {
}
