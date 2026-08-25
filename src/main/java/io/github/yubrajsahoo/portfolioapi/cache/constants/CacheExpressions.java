/*
 *
 *  * Copyright (c) 2026 Yubraj Sahoo. All rights reserved.
 *
 */

package io.github.yubrajsahoo.portfolioapi.cache.constants;

import lombok.experimental.UtilityClass;

/**
 * Utility class containing Spring Expression Language (SpEL) expressions used for caching.
 * <p>
 * These expressions are primarily used to dynamically generate cache keys within
 * Spring's caching annotations (e.g., {@code @Cacheable}, {@code @CacheEvict}).
 * </p>
 *
 * @author Yubraj Sahoo
 * @since 0.0.1-SNAPSHOT
 */
@UtilityClass
public class CacheExpressions {

    /**
     * SpEL expression for generating a cache key for retrieving cloud URLs.
     * <p>
     * It constructs the key by concatenating the {@code accessType} and the {@code fileName}.
     * If {@code accessType} is null, it defaults to {@code AccessType.PUBLIC}.
     * </p>
     */
    public static final String CLOUD_GET_URL = "(#accessType ?: T(io.github.yubrajsahoo.portfolioapi.enums.AccessType).PUBLIC) + '-' + #fileName";
}
