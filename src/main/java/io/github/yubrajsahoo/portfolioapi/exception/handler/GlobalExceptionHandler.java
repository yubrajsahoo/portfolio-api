/*
 *
 *  * Copyright (c) 2026 Yubraj Sahoo. All rights reserved.
 *
 */

package io.github.yubrajsahoo.portfolioapi.exception.handler;

import jakarta.validation.ConstraintViolationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.Instant;
import java.util.HashMap;
import java.util.Map;

/**
 * Global exception handler for the Portfolio API.
 * <p>
 * This class catches specific exceptions thrown by the application and transforms
 * them into standardized {@link ProblemDetail} responses.
 * </p>
 *
 * @author Yubraj Sahoo
 * @since 0.0.1-SNAPSHOT
 */
@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    /**
     * Handles {@link io.github.yubrajsahoo.portfolioapi.exception.CloudinaryException}.
     *
     * @param ex the CloudinaryException that was thrown
     * @return a {@link ProblemDetail} with HTTP status 503 (Service Unavailable)
     */
    @ExceptionHandler(io.github.yubrajsahoo.portfolioapi.exception.CloudinaryException.class)
    public ProblemDetail handleCloudinaryException(io.github.yubrajsahoo.portfolioapi.exception.CloudinaryException ex) {
        log.error("Cloudinary exception occurred: {}", ex.getMessage());

        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(
                HttpStatus.SERVICE_UNAVAILABLE,
                "Service Not Available"
        );

        problemDetail.setTitle("Service Not Available");
        problemDetail.setProperty("timestamp", Instant.now());
        return problemDetail;
    }

    /**
     * Handles {@link io.github.yubrajsahoo.portfolioapi.exception.FileUploadException}.
     *
     * @param ex the FileUploadException that was thrown
     * @return a {@link ProblemDetail} with HTTP status 422 (Unprocessable Content)
     */
    @ExceptionHandler(io.github.yubrajsahoo.portfolioapi.exception.FileUploadException.class)
    public ProblemDetail handleFileUploadException(io.github.yubrajsahoo.portfolioapi.exception.FileUploadException ex) {
        log.error("File upload exception occurred: {}", ex.getMessage());

        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(
                HttpStatus.UNPROCESSABLE_CONTENT,
                "Unable To Upload File"
        );

        problemDetail.setTitle("Unable To Upload File");
        problemDetail.setProperty("timestamp", Instant.now());
        return problemDetail;
    }

    /**
     * Handles {@link ConstraintViolationException}.
     *
     * @param ex the ConstraintViolationException that was thrown
     * @return a {@link ProblemDetail} with HTTP status 400 (Bad Request) and a list of field errors
     */
    @ExceptionHandler(ConstraintViolationException.class)
    public ProblemDetail handleConstraintViolationException(ConstraintViolationException ex) {
        log.error("Constraint violation exception occurred: {}", ex.getMessage());

        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(
                HttpStatus.BAD_REQUEST,
                "Validation failed"
        );

        problemDetail.setTitle("Bad Request");

        Map<String, String> errors = new HashMap<>();
        ex.getConstraintViolations().forEach(violation -> {
            String propertyPath = violation.getPropertyPath().toString();
            // Usually format is methodName.parameterName, we just want parameterName
            String field = propertyPath.contains(".") ? propertyPath.substring(propertyPath.lastIndexOf('.') + 1) : propertyPath;
            errors.put(field, violation.getMessage());
        });

        problemDetail.setProperty("errors", errors);
        problemDetail.setProperty("timestamp", Instant.now());
        return problemDetail;
    }

    /**
     * Handles {@link IllegalArgumentException}.
     *
     * @param ex the IllegalArgumentException that was thrown
     * @return a {@link ProblemDetail} with HTTP status 400 (Bad Request)
     */
    @ExceptionHandler(IllegalArgumentException.class)
    public ProblemDetail handleIllegalArgumentException(IllegalArgumentException ex) {
        log.error("Illegal argument exception occurred: {}", ex.getMessage());

        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(
                HttpStatus.BAD_REQUEST,
                ex.getMessage()
        );

        problemDetail.setTitle("Bad Request");
        problemDetail.setProperty("timestamp", Instant.now());
        return problemDetail;
    }

    /**
     * Handles all uncaught {@link Exception}s.
     *
     * @param ex the Exception that was thrown
     * @return a {@link ProblemDetail} with HTTP status 500 (Internal Server Error)
     */
    @ExceptionHandler(Exception.class)
    public ProblemDetail handleAllUncaughtException(Exception ex) {
        log.error("Unknown internal server error occurred: {}", ex.getMessage(), ex);

        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(
                HttpStatus.INTERNAL_SERVER_ERROR,
                "An unexpected error occurred"
        );

        problemDetail.setTitle("Internal Server Error");
        problemDetail.setProperty("timestamp", Instant.now());
        return problemDetail;
    }
}
