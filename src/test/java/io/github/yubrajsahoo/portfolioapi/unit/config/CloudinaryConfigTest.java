package io.github.yubrajsahoo.portfolioapi.unit.config;

import com.cloudinary.Cloudinary;
import io.github.yubrajsahoo.portfolioapi.config.CloudinaryConfig;
import io.github.yubrajsahoo.portfolioapi.config.CloudinaryProperties;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertNotNull;

@DisplayName("Unit: Cloudinary Config")
class CloudinaryConfigTest {

    @Test
    @DisplayName("Should Create Cloudinary Bean")
    void cloudinaryBean() {
        CloudinaryConfig config = new CloudinaryConfig();
        CloudinaryProperties properties = new CloudinaryProperties("cloud", "key", "secret", null);
        Cloudinary cloudinary = config.cloudinary(properties);
        assertNotNull(cloudinary);
    }
}
