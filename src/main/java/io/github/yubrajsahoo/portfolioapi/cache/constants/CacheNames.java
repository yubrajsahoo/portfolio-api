/*
 *
 *  * Copyright (c) 2026 Yubraj Sahoo. All rights reserved.
 *
 */

package io.github.yubrajsahoo.portfolioapi.cache.constants;

import lombok.experimental.UtilityClass;

/**
 * Utility class containing constant names for different caches used in the application.
 * <p>
 * These constants are meant to be used with Spring caching annotations such as
 * {@code @Cacheable}, {@code @CacheEvict}, and {@code @CachePut} to specify the
 * target cache.
 * </p>
 *
 * @author Yubraj Sahoo
 * @since 0.0.1-SNAPSHOT
 */
@UtilityClass
public class CacheNames {

    /**
     * Cache name for storing and retrieving generated cloud file URLs.
     */
    public static final String CLOUD_FILE_URL = "cloud-file-urls";

    /**
     * Cache name for storing and retrieving the complete list of all cloud files.
     */
    public static final String ALL_CLOUD_FILES = "all-cloud-files";

}
