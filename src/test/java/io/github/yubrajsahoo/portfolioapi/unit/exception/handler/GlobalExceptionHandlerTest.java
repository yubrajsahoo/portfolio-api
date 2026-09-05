package io.github.yubrajsahoo.portfolioapi.unit.exception.handler;

import io.github.yubrajsahoo.portfolioapi.exception.CloudinaryException;
import io.github.yubrajsahoo.portfolioapi.exception.FileUploadException;
import io.github.yubrajsahoo.portfolioapi.exception.handler.GlobalExceptionHandler;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.Path;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.servlet.resource.NoResourceFoundException;

import java.util.Map;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class GlobalExceptionHandlerTest {

    private GlobalExceptionHandler exceptionHandler;

    @BeforeEach
    void setUp() {
        exceptionHandler = new GlobalExceptionHandler();
    }

    @Test
    void testHandleCloudinaryException() {
        CloudinaryException ex = new CloudinaryException("Cloudinary error", io.github.yubrajsahoo.portfolioapi.metrics.MetricsType.ERROR, new RuntimeException());
        ProblemDetail problemDetail = exceptionHandler.handleCloudinaryException(ex);

        assertEquals(HttpStatus.SERVICE_UNAVAILABLE.value(), problemDetail.getStatus());
        assertEquals("Service Not Available", problemDetail.getDetail());
        assertEquals("Service Not Available", problemDetail.getTitle());
        assertNotNull(problemDetail.getProperties().get("timestamp"));
    }

    @Test
    void testHandleFileUploadException() {
        FileUploadException ex = new FileUploadException("Upload error", new RuntimeException());
        ProblemDetail problemDetail = exceptionHandler.handleFileUploadException(ex);

        assertEquals(HttpStatus.UNPROCESSABLE_CONTENT.value(), problemDetail.getStatus());
        assertEquals("Unable To Upload File", problemDetail.getDetail());
        assertEquals("Unable To Upload File", problemDetail.getTitle());
        assertNotNull(problemDetail.getProperties().get("timestamp"));
    }

    @Test
    @SuppressWarnings("unchecked")
    void testHandleConstraintViolationException() {
        ConstraintViolation<?> violation = mock(ConstraintViolation.class);
        Path path = mock(Path.class);
        when(path.toString()).thenReturn("methodName.parameterName");
        when(violation.getPropertyPath()).thenReturn(path);
        when(violation.getMessage()).thenReturn("must not be null");

        ConstraintViolationException ex = new ConstraintViolationException("Validation failed", Set.of(violation));
        ProblemDetail problemDetail = exceptionHandler.handleConstraintViolationException(ex);

        assertEquals(HttpStatus.BAD_REQUEST.value(), problemDetail.getStatus());
        assertEquals("Validation failed", problemDetail.getDetail());
        assertEquals("Bad Request", problemDetail.getTitle());
        assertNotNull(problemDetail.getProperties().get("timestamp"));
        
        Map<String, String> errors = (Map<String, String>) problemDetail.getProperties().get("errors");
        assertNotNull(errors);
        assertEquals("must not be null", errors.get("parameterName"));
    }

    @Test
    @SuppressWarnings("unchecked")
    void testHandleConstraintViolationExceptionWithoutDot() {
        ConstraintViolation<?> violation = mock(ConstraintViolation.class);
        Path path = mock(Path.class);
        when(path.toString()).thenReturn("parameterName");
        when(violation.getPropertyPath()).thenReturn(path);
        when(violation.getMessage()).thenReturn("must not be null");

        ConstraintViolationException ex = new ConstraintViolationException("Validation failed", Set.of(violation));
        ProblemDetail problemDetail = exceptionHandler.handleConstraintViolationException(ex);

        Map<String, String> errors = (Map<String, String>) problemDetail.getProperties().get("errors");
        assertNotNull(errors);
        assertEquals("must not be null", errors.get("parameterName"));
    }

    @Test
    void testHandleIllegalArgumentException() {
        IllegalArgumentException ex = new IllegalArgumentException("Invalid argument");
        ProblemDetail problemDetail = exceptionHandler.handleIllegalArgumentException(ex);

        assertEquals(HttpStatus.BAD_REQUEST.value(), problemDetail.getStatus());
        assertEquals("Invalid argument", problemDetail.getDetail());
        assertEquals("Bad Request", problemDetail.getTitle());
        assertNotNull(problemDetail.getProperties().get("timestamp"));
    }

    @Test
    void testHandleHttpRequestMethodNotSupportedException() {
        HttpRequestMethodNotSupportedException ex = new HttpRequestMethodNotSupportedException("POST");
        ProblemDetail problemDetail = exceptionHandler.handleHttpRequestMethodNotSupportedException(ex);

        assertEquals(HttpStatus.BAD_REQUEST.value(), problemDetail.getStatus());
        assertEquals("Request method 'POST' is not supported", problemDetail.getDetail());
        assertEquals("Invalid Endpoint", problemDetail.getTitle());
        assertNotNull(problemDetail.getProperties().get("timestamp"));
    }

    @Test
    void testHandleNoResourceFoundException() {
        NoResourceFoundException ex = mock(NoResourceFoundException.class);
        when(ex.getMessage()).thenReturn("No static resource /path.");
        ProblemDetail problemDetail = exceptionHandler.handleHttpRequestMethodNotSupportedException(ex);

        assertEquals(HttpStatus.BAD_REQUEST.value(), problemDetail.getStatus());
        assertTrue(problemDetail.getDetail().contains("No static resource /path."));
        assertEquals("Invalid Endpoint", problemDetail.getTitle());
        assertNotNull(problemDetail.getProperties().get("timestamp"));
    }

    @Test
    void testHandleAllUncaughtException() {
        Exception ex = new Exception("Unknown error");
        ProblemDetail problemDetail = exceptionHandler.handleAllUncaughtException(ex);

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR.value(), problemDetail.getStatus());
        assertEquals("An unexpected error occurred", problemDetail.getDetail());
        assertEquals("Internal Server Error", problemDetail.getTitle());
        assertNotNull(problemDetail.getProperties().get("timestamp"));
    }

    @Test
    void testHandleRegistrationException() {
        io.github.yubrajsahoo.portfolioapi.exception.RegistrationException ex = new io.github.yubrajsahoo.portfolioapi.exception.RegistrationException("User already exists");
        ProblemDetail problemDetail = exceptionHandler.handleRegistrationException(ex);

        assertEquals(HttpStatus.BAD_REQUEST.value(), problemDetail.getStatus());
        assertEquals("User already exists", problemDetail.getDetail());
        assertEquals("Registration Failed", problemDetail.getTitle());
        assertNotNull(problemDetail.getProperties().get("timestamp"));
    }

    @Test
    void testHandlePortfolioApiException() {
        io.github.yubrajsahoo.portfolioapi.exception.base.PortfolioApiException ex = new io.github.yubrajsahoo.portfolioapi.exception.base.PortfolioApiException("Something went wrong");
        ProblemDetail problemDetail = exceptionHandler.handlePortfolioApiException(ex);

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR.value(), problemDetail.getStatus());
        assertEquals("Something went wrong", problemDetail.getDetail());
        assertEquals("Application Error", problemDetail.getTitle());
        assertNotNull(problemDetail.getProperties().get("timestamp"));
    }
}
