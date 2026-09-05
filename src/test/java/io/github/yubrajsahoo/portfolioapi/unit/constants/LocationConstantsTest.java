package io.github.yubrajsahoo.portfolioapi.unit.constants;

import io.github.yubrajsahoo.portfolioapi.constants.LocationConstants;
import org.junit.jupiter.api.Test;

import java.time.ZoneId;

import static org.junit.jupiter.api.Assertions.assertEquals;

class LocationConstantsTest {
    @Test
    void testLocationConstant() {
        assertEquals(ZoneId.of("Asia/Kolkata"), LocationConstants.SERVER_LOCATION);
    }
}
