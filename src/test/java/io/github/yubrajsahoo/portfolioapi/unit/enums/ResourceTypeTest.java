package io.github.yubrajsahoo.portfolioapi.unit.enums;

import io.github.yubrajsahoo.portfolioapi.enums.ResourceType;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ResourceTypeTest {

    @Test
    void testFromExtension() {
        assertEquals(ResourceType.IMAGE, ResourceType.fromExtension("png"));
        assertEquals(ResourceType.VIDEO, ResourceType.fromExtension("mp4"));
        assertEquals(ResourceType.RAW, ResourceType.fromExtension("docx"));
        assertEquals(ResourceType.RAW, ResourceType.fromExtension("unknown"));
        assertEquals(ResourceType.RAW, ResourceType.fromExtension(null));
        assertEquals(ResourceType.RAW, ResourceType.fromExtension(""));
        assertEquals(ResourceType.RAW, ResourceType.fromExtension("  "));
    }

    @Test
    void testFromCloudinary() {
        assertEquals(ResourceType.IMAGE, ResourceType.fromCloudinary("image"));
        assertEquals(ResourceType.VIDEO, ResourceType.fromCloudinary("video"));
        assertEquals(ResourceType.RAW, ResourceType.fromCloudinary("raw"));
        assertEquals(ResourceType.AUTO, ResourceType.fromCloudinary("auto"));
        assertEquals(ResourceType.RAW, ResourceType.fromCloudinary("unknown"));
        assertEquals(ResourceType.RAW, ResourceType.fromCloudinary(null));
    }
}
