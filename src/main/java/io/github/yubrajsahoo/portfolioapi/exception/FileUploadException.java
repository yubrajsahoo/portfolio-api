/*
 *
 *  * Copyright (c) 2026 Yubraj Sahoo. All rights reserved.
 *
 */

package io.github.yubrajsahoo.portfolioapi.exception;

import io.github.yubrajsahoo.portfolioapi.exception.base.PortfolioApiException;
import io.github.yubrajsahoo.portfolioapi.metrics.MetricsType;

/**
 * Exception while uploading file.
 *
 * @author Yubraj Sahoo
 * @version 0.0.1-Snapshot
 */
public class FileUploadException extends PortfolioApiException {
    /**
     * Constructs a new {@code FileUploadException} with the specified detail message
     * and the default metric type ({@code MetricsType.ERROR}).
     *
     * @param message the detail message explaining the error
     */
    public FileUploadException(String message) {
        super(message);
    }

    /**
     * Constructs a new {@code FileUploadException} with the specified metric and detail message.
     *
     * @param message     the detail message explaining the error
     * @param metricsType the standardized metric identifier for this error
     */
    public FileUploadException(String message, MetricsType metricsType) {
        super(message, metricsType);
    }

    /**
     * Constructs a new {@code FileUploadException} with the specified detail message and
     * underlying cause, using the default metric type ({@code MetricsType.ERROR}).
     *
     * @param message the detail message explaining the error
     * @param cause   the underlying cause of the exception
     */
    public FileUploadException(String message, Throwable cause) {
        super(message, cause);
    }

    /**
     * Constructs a new {@code FileUploadException} with the specified metric, detail message,
     * and underlying cause.
     * <p>
     * This is useful for wrapping lower-level system exceptions (e.g., IOExceptions,
     * SQLExceptions) while preserving their original stack trace for debugging.
     *
     * @param message     the detail message explaining the error
     * @param metricsType the standardized metric identifier for this error
     * @param cause       the underlying cause of the exception
     */
    public FileUploadException(String message, MetricsType metricsType, Throwable cause) {
        super(message, metricsType, cause);
    }
}
