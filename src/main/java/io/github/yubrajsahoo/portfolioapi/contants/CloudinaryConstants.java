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
     * Cloudinary portfolio folder.
     */
    public static final String PORTFOLIO_FOLDER = "portfolio";

    /**
     * Cloudinary public ID parameter.
     */
    public static final String PUBLIC_ID = "public_id";

    /**
     * Cloudinary resource type parameter.
     */
    public static final String RESOURCE_TYPE = "resource_type";

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
}
