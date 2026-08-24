package io.github.yubrajsahoo.portfolioapi.unit.utils;

import com.cloudinary.utils.ObjectUtils;
import io.github.yubrajsahoo.portfolioapi.enums.AccessType;
import io.github.yubrajsahoo.portfolioapi.enums.ResourceType;
import io.github.yubrajsahoo.portfolioapi.exception.CloudinaryException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Map;

import static io.github.yubrajsahoo.portfolioapi.utils.CloudinaryUtils.*;
import static org.junit.jupiter.api.Assertions.*;

@DisplayName("CloudinaryUtils Unit Test")
class CloudinaryUtilsTest {

    @Test
    @DisplayName("Test buildUploadOption with a PNG file")
    void testBuildUploadOption_withPngFile() {
        // Given
        String fileName = "test.png";
        AccessType accessType = AccessType.PUBLIC;

        // When
        Map<?, ?> uploadOptions = buildUploadOption(fileName, accessType);

        // Then
        assertEquals("portfolio/upload/image/test.png", uploadOptions.get("public_id"));
        assertEquals("image", uploadOptions.get("resource_type"));
        assertEquals("upload", uploadOptions.get("type"));
        assertEquals(true, uploadOptions.get("overwrite"));
    }

    @Test
    @DisplayName("Test buildPublicId")
    void testBuildPublicId() {
        // Given
        AccessType accessType = AccessType.PRIVATE;
        ResourceType resourceType = ResourceType.VIDEO;
        String fileName = "video.mp4";

        // When
        String publicId = buildPublicId(accessType, resourceType, fileName);

        // Then
        assertEquals("portfolio/authenticated/video/video.mp4", publicId);
    }

    @Test
    @DisplayName("Test extractSecureUrl with a valid result")
    void testExtractSecureUrl_withValidResult() {
        // Given
        String expectedUrl = "http://res.cloudinary.com/demo/image/upload/v1571218039/sample.jpg";
        Map uploadResult = ObjectUtils.asMap("secure_url", expectedUrl);
        String fileName = "sample.jpg";

        // When
        String actualUrl = extractSecureUrl(uploadResult, fileName);

        // Then
        assertEquals(expectedUrl, actualUrl);
    }

    @Test
    @DisplayName("Test extractSecureUrl with a null result")
    void testExtractSecureUrl_withNullResult() {
        // Given
        String fileName = "sample.jpg";

        // Then
        assertThrows(CloudinaryException.class, () -> extractSecureUrl(null, fileName));
    }

    @Test
    @DisplayName("Test extractSecureUrl with a result missing the secure_url key")
    void testExtractSecureUrl_withMissingSecureUrl() {
        // Given
        Map uploadResult = ObjectUtils.asMap("url", "http://res.cloudinary.com/demo/image/upload/v1571218039/sample.jpg");
        String fileName = "sample.jpg";

        // Then
        assertThrows(CloudinaryException.class, () -> extractSecureUrl(uploadResult, fileName));
    }

    @Test
    @DisplayName("Test buildUploadOption with an unsupported file extension")
    void testBuildUploadOption_withUnsupportedExtension() {
        // Given
        String fileName = "test.unsupported";
        AccessType accessType = AccessType.PUBLIC;

        // When
        Map<?, ?> uploadOptions = buildUploadOption(fileName, accessType);

        // Then
        assertEquals("portfolio/upload/raw/test.unsupported", uploadOptions.get("public_id"));
        assertEquals("raw", uploadOptions.get("resource_type"));
        assertEquals("upload", uploadOptions.get("type"));
        assertEquals(true, uploadOptions.get("overwrite"));
    }
}