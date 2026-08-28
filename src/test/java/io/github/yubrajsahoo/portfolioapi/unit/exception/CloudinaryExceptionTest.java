package io.github.yubrajsahoo.portfolioapi.unit.exception;

import io.github.yubrajsahoo.portfolioapi.exception.CloudinaryException;
import io.github.yubrajsahoo.portfolioapi.metrics.MetricsType;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;

class CloudinaryExceptionTest {

    @Test
    void testConstructorWithMessage() {
        String message = "Cloudinary error";
        CloudinaryException exception = new CloudinaryException(message);
        
        assertEquals(message, exception.getMessage());
        assertEquals(MetricsType.ERROR, exception.getMetricsType());
        assertNull(exception.getCause());
    }

    @Test
    void testConstructorWithMessageAndMetricsType() {
        String message = "Cloudinary error";
        MetricsType type = MetricsType.ERROR;
        CloudinaryException exception = new CloudinaryException(message, type);
        
        assertEquals(message, exception.getMessage());
        assertEquals(type, exception.getMetricsType());
        assertNull(exception.getCause());
    }

    @Test
    void testConstructorWithMessageMetricsTypeAndCause() {
        String message = "Cloudinary error";
        MetricsType type = MetricsType.ERROR;
        Throwable cause = new RuntimeException("Network error");
        CloudinaryException exception = new CloudinaryException(message, type, cause);
        
        assertEquals(message, exception.getMessage());
        assertEquals(type, exception.getMetricsType());
        assertSame(cause, exception.getCause());
    }
}
