/*
 *
 *  * Copyright (c) 2026 Yubraj Sahoo. All rights reserved.
 *
 */

package io.github.yubrajsahoo.portfolioapi.metrics;

/**
 * Defines standard metric types for tracking application behavior, success rates,
 * and error categorizations across the Portfolio API.
 *
 * @author Yubraj Sahoo
 * @since 0.0.1-SNAPSHOT
 */
public enum MetricsType {

    /**
     * Indicates a successful operation.
     */
    SUCCESS,

    /**
     * Indicates a generic or internal application error.
     */
    ERROR,

    /**
     * Indicates an error caused by an external service or dependency (e.g., Cloudinary, Database).
     */
    EXTERNAL_ERROR,

    BAD_REQUEST,
    /**
     * Failure While Performing task
     */
    FAILURE
}
