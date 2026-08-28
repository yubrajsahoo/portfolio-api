package io.github.yubrajsahoo.portfolioapi.unit.exception.base;

import io.github.yubrajsahoo.portfolioapi.exception.base.PortfolioApiException;
import io.github.yubrajsahoo.portfolioapi.metrics.MetricsType;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;

class PortfolioApiExceptionTest {

    @Test
    void testConstructorWithMessage() {
        String message = "Test message";
        PortfolioApiException exception = new PortfolioApiException(message);
        
        assertEquals(message, exception.getMessage());
        assertEquals(MetricsType.ERROR, exception.getMetricsType());
        assertNull(exception.getCause());
    }

    @Test
    void testConstructorWithMessageAndMetricsType() {
        String message = "Test message";
        MetricsType type = MetricsType.ERROR; // Assuming MetricsType.ERROR exists
        PortfolioApiException exception = new PortfolioApiException(message, type);
        
        assertEquals(message, exception.getMessage());
        assertEquals(type, exception.getMetricsType());
        assertNull(exception.getCause());
    }

    @Test
    void testConstructorWithMessageAndCause() {
        String message = "Test message";
        Throwable cause = new RuntimeException("Cause message");
        PortfolioApiException exception = new PortfolioApiException(message, cause);
        
        assertEquals(message, exception.getMessage());
        assertEquals(MetricsType.ERROR, exception.getMetricsType());
        assertSame(cause, exception.getCause());
    }

    @Test
    void testConstructorWithMessageMetricsTypeAndCause() {
        String message = "Test message";
        MetricsType type = MetricsType.ERROR;
        Throwable cause = new RuntimeException("Cause message");
        PortfolioApiException exception = new PortfolioApiException(message, type, cause);
        
        assertEquals(message, exception.getMessage());
        assertEquals(type, exception.getMetricsType());
        assertSame(cause, exception.getCause());
    }
}
