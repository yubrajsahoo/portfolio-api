package io.github.yubrajsahoo.portfolioapi.unit.exception;

import io.github.yubrajsahoo.portfolioapi.exception.FileUploadException;
import io.github.yubrajsahoo.portfolioapi.metrics.MetricsType;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;

class FileUploadExceptionTest {

    @Test
    void testConstructorWithMessage() {
        String message = "Upload failed";
        FileUploadException exception = new FileUploadException(message);
        
        assertEquals(message, exception.getMessage());
        assertEquals(MetricsType.ERROR, exception.getMetricsType());
        assertNull(exception.getCause());
    }

    @Test
    void testConstructorWithMessageAndMetricsType() {
        String message = "Upload failed";
        MetricsType type = MetricsType.ERROR;
        FileUploadException exception = new FileUploadException(message, type);
        
        assertEquals(message, exception.getMessage());
        assertEquals(type, exception.getMetricsType());
        assertNull(exception.getCause());
    }

    @Test
    void testConstructorWithMessageAndCause() {
        String message = "Upload failed";
        Throwable cause = new RuntimeException("Network error");
        FileUploadException exception = new FileUploadException(message, cause);
        
        assertEquals(message, exception.getMessage());
        assertEquals(MetricsType.ERROR, exception.getMetricsType());
        assertSame(cause, exception.getCause());
    }

    @Test
    void testConstructorWithMessageMetricsTypeAndCause() {
        String message = "Upload failed";
        MetricsType type = MetricsType.ERROR;
        Throwable cause = new RuntimeException("Network error");
        FileUploadException exception = new FileUploadException(message, type, cause);
        
        assertEquals(message, exception.getMessage());
        assertEquals(type, exception.getMetricsType());
        assertSame(cause, exception.getCause());
    }
}
