package io.github.yubrajsahoo.portfolioapi.unit.exception;

import io.github.yubrajsahoo.portfolioapi.exception.RegistrationException;
import io.github.yubrajsahoo.portfolioapi.metrics.MetricsType;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

@DisplayName("Unit: Registration Exception")
class RegistrationExceptionTest {

    @Test
    void testMessageConstructor() {
        RegistrationException exception = new RegistrationException("Error message");
        assertEquals("Error message", exception.getMessage());
        assertEquals(MetricsType.ERROR, exception.getMetricsType());
    }

    @Test
    void testMessageAndMetricsTypeConstructor() {
        RegistrationException exception = new RegistrationException("Error message", MetricsType.BAD_REQUEST);
        assertEquals("Error message", exception.getMessage());
        assertEquals(MetricsType.BAD_REQUEST, exception.getMetricsType());
    }

    @Test
    void testMessageAndCauseConstructor() {
        Throwable cause = new RuntimeException("Cause message");
        RegistrationException exception = new RegistrationException("Error message", cause);
        assertEquals("Error message", exception.getMessage());
        assertEquals(cause, exception.getCause());
        assertEquals(MetricsType.ERROR, exception.getMetricsType());
    }

    @Test
    void testMessageMetricsTypeAndCauseConstructor() {
        Throwable cause = new RuntimeException("Cause message");
        RegistrationException exception = new RegistrationException("Error message", MetricsType.BAD_REQUEST, cause);
        assertEquals("Error message", exception.getMessage());
        assertEquals(cause, exception.getCause());
        assertEquals(MetricsType.BAD_REQUEST, exception.getMetricsType());
    }
}
