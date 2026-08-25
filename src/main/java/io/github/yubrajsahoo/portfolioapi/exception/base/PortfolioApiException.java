/*
 *
 *  * Copyright (c) 2026 Yubraj Sahoo. All rights reserved.
 *
 */

package io.github.yubrajsahoo.portfolioapi.exception.base;

import io.github.yubrajsahoo.portfolioapi.metrics.MetricsType;
import lombok.Getter;

/**
 * Base custom exception for the Portfolio API.
 * This exception encapsulates a specific {@link MetricsType} identifier alongside
 * the standard exception message and cause, making it easier to track and
 * standardize application errors globally.
 *
 * @author Yubraj Sahoo
 * @since 0.0.1-SNAPSHOT
 */
@Getter
public class PortfolioApiException extends RuntimeException {

    /**
     * The metric identifier associated with this exception.
     */
    private MetricsType metricsType = MetricsType.ERROR;

    /**
     * Constructs a new {@code PortfolioApiException} with the specified detail message
     * and the default metric type ({@code MetricsType.ERROR}).
     *
     * @param message the detail message explaining the error
     */
    public PortfolioApiException(String message) {
        super(message);
    }

    /**
     * Constructs a new {@code PortfolioApiException} with the specified metric and detail message.
     *
     * @param message     the detail message explaining the error
     * @param metricsType the standardized metric identifier for this error
     */
    public PortfolioApiException(String message, MetricsType metricsType) {
        super(message);
        this.metricsType = metricsType;
    }

    /**
     * Constructs a new {@code PortfolioApiException} with the specified detail message and
     * underlying cause, using the default metric type ({@code MetricsType.ERROR}).
     *
     * @param message the detail message explaining the error
     * @param cause   the underlying cause of the exception
     */
    public PortfolioApiException(String message, Throwable cause) {
        super(message, cause);
    }

    /**
     * Constructs a new {@code PortfolioApiException} with the specified metric, detail message,
     * and underlying cause.
     * <p>
     * This is useful for wrapping lower-level system exceptions (e.g., IOExceptions,
     * SQLExceptions) while preserving their original stack trace for debugging.
     *
     * @param message     the detail message explaining the error
     * @param metricsType the standardized metric identifier for this error
     * @param cause       the underlying cause of the exception
     */
    public PortfolioApiException(String message, MetricsType metricsType, Throwable cause) {
        super(message, cause);
        this.metricsType = metricsType;
    }
}
