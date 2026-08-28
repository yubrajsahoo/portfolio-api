package io.github.yubrajsahoo.portfolioapi.unit.enums;

import io.github.yubrajsahoo.portfolioapi.enums.AccessType;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class AccessTypeTest {

    @Test
    void testFromCloudinary() {
        assertEquals(AccessType.PUBLIC, AccessType.fromCloudinary("upload"));
        assertEquals(AccessType.PRIVATE, AccessType.fromCloudinary("authenticated"));
        assertEquals(AccessType.PUBLIC, AccessType.fromCloudinary("unknown"));
        assertEquals(AccessType.PUBLIC, AccessType.fromCloudinary(null));
    }
}
