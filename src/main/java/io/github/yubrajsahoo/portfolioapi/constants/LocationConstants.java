/*
 *
 *  * Copyright (c) 2026 Yubraj Sahoo. All rights reserved.
 *
 */

package io.github.yubrajsahoo.portfolioapi.constants;

import lombok.experimental.UtilityClass;

import java.time.ZoneId;

/**
 * Utility class containing location-related constants.
 */
@UtilityClass
public class LocationConstants {

    /**
     * The default server location timezone.
     */
    public static ZoneId SERVER_LOCATION = ZoneId.of("Asia/Kolkata");
}
