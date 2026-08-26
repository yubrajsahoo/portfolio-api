/*
 *
 *  * Copyright (c) 2026 Yubraj Sahoo. All rights reserved.
 *
 */

package io.github.yubrajsahoo.portfolioapi.exception;

import io.github.yubrajsahoo.portfolioapi.exception.base.PortfolioApiException;
import io.github.yubrajsahoo.portfolioapi.metrics.MetricsType;

/**
 * Exception thrown when an error occurs while interacting with Cloudinary.
 *
 * @author Yubraj Sahoo
 * @since 0.0.1-SNAPSHOT
 */
public class CloudinaryException extends PortfolioApiException {

    /**
     * Constructs a new {@code CloudinaryException} with the specified detail message
     * and the default metric type ({@code MetricsType.ERROR}).
     *
     * @param message the detail message explaining the error
     */
    public CloudinaryException(String message) {
        super(message);
    }

    /**
     * Constructs a new {@code CloudinaryException} with the specified metric and detail message.
     *
     * @param message     the detail message explaining the error
     * @param metricsType the standardized metric identifier for this error
     */
    public CloudinaryException(String message, MetricsType metricsType) {
        super(message, metricsType);
    }

    /**
     * Constructs a new {@code CloudinaryException} with the specified metric, detail message,
     * and underlying cause.
     * <p>
     * This is useful for wrapping lower-level system exceptions (e.g., IOExceptions,
     * SQLExceptions) while preserving their original stack trace for debugging.
     *
     * @param message     the detail message explaining the error
     * @param metricsType the standardized metric identifier for this error
     * @param cause       the underlying cause of the exception
     */
    public CloudinaryException(String message, MetricsType metricsType, Throwable cause) {
        super(message, metricsType, cause);
    }
}
