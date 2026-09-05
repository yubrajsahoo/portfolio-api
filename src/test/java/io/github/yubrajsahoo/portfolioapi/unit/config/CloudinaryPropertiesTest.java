package io.github.yubrajsahoo.portfolioapi.unit.config;

import io.github.yubrajsahoo.portfolioapi.config.CloudinaryProperties;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.Duration;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@DisplayName("Unit: Cloudinary Properties")
class CloudinaryPropertiesTest {

    @Test
    @DisplayName("Should Set Default privateUrlTtl When Null")
    void constructor_NullTtl() {
        CloudinaryProperties properties = new CloudinaryProperties("cloud", "key", "secret", null);
        assertEquals(Duration.ofMinutes(5), properties.privateUrlTtl());
    }

    @Test
    @DisplayName("Should Use Provided privateUrlTtl When Not Null")
    void constructor_NotNullTtl() {
        Duration customTtl = Duration.ofMinutes(10);
        CloudinaryProperties properties = new CloudinaryProperties("cloud", "key", "secret", customTtl);
        assertEquals(customTtl, properties.privateUrlTtl());
    }

    @Test
    @DisplayName("Should Return Config Map")
    void toMap() {
        CloudinaryProperties properties = new CloudinaryProperties("my-cloud", "my-key", "my-secret", null);
        Map<String, String> map = properties.toMap();
        
        assertNotNull(map);
        assertEquals(3, map.size());
        assertEquals("my-cloud", map.get("cloud_name"));
        assertEquals("my-key", map.get("api_key"));
        assertEquals("my-secret", map.get("api_secret"));
    }
}
