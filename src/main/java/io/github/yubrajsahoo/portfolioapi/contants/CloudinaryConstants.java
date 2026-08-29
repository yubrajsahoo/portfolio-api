/*
 *
 *  * Copyright (c) 2026 Yubraj Sahoo. All rights reserved.
 *
 */

package io.github.yubrajsahoo.portfolioapi.contants;

import lombok.experimental.UtilityClass;

/**
 * Constants for Cloudinary integration.
 *
 * @author Yubraj Sahoo
 * @since 0.0.1-SNAPSHOT
 */
@UtilityClass
public class CloudinaryConstants {
    /**
     * Cloudinary public ID parameter.
     */
    public static final String PUBLIC_ID = "public_id";

    /**
     * Cloudinary resource type parameter.
     */
    public static final String RESOURCE_TYPE = "resource_type";

    /**
     * Cloudinary resources response field.
     */
    public static final String RESOURCES = "resources";

    /**
     * Cloudinary delivery type parameter.
     */
    public static final String TYPE = "type";

    /**
     * Cloudinary overwrite parameter.
     */
    public static final String OVERWRITE = "overwrite";

    /**
     * Cloudinary secure URL response field.
     */
    public static final String SECURE_URL = "secure_url";

    /**
     * Cloudinary expires_at parameter.
     */
    public static final String EXPIRES_AT = "expires_at";

    /**
     * Format string for generating a public ID using folder and file name.
     */
    public static final String PUBLIC_ID_FORMAT = "%s/%s";

    /**
     * Cloudinary prefix parameter used for filtering resources.
     */
    public static final String PREFIX = "prefix";

    /**
     * Cloudinary max_results parameter.
     */
    public static final String MAX_RESULT = "max_results";

    /**
     * Regular expression used to parse and extract components from a Cloudinary URL.
     */
    public static final String REGEX_CLOUDINARY_ULR = "([^/]+)/([^/]+)/([^/]+)/([^/]+)$";
}
