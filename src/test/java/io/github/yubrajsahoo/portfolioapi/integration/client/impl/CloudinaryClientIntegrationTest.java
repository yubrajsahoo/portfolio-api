package io.github.yubrajsahoo.portfolioapi.integration.client.impl;

import io.github.yubrajsahoo.portfolioapi.DataBuilderUtils;
import io.github.yubrajsahoo.portfolioapi.client.impl.CloudinaryClient;
import io.github.yubrajsahoo.portfolioapi.enums.AccessType;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.InputStream;

import static org.junit.jupiter.api.Assertions.assertNotNull;

@Slf4j
@SpringBootTest
public class CloudinaryClientIntegrationTest {

    @Autowired
    private CloudinaryClient cloudinaryClient;

    @Test
    @DisplayName("Test upload file in public")
    @Disabled("Due to storing data in Cloudinary")
    void testUploadPublic() {
        InputStream inputStream = DataBuilderUtils.readFile("src/test/resources/images/logo.png");
        String fileName = "logo.png";

        String publicId = cloudinaryClient.upload(AccessType.PUBLIC, fileName, inputStream);
        log.info("Public File ID:{}", publicId);
        assertNotNull(publicId);
    }

    @Test
    @DisplayName("Test upload file in private")
    @Disabled("Due to storing data in Cloudinary")
    void testUploadPrivate() {
        InputStream inputStream = DataBuilderUtils.readFile("src/test/resources/pdf/Yubraj-Resume.pdf");
        String fileName = "Yubraj-Resume.pdf";

        String publicId = cloudinaryClient.upload(AccessType.PRIVATE, fileName, inputStream);
        log.info("Private File ID:{}", publicId);
        assertNotNull(publicId);
    }

    @Test
    @DisplayName("Test to generate download url for public")
    void testGeneratePublicUrl() {
        String fileName = "logo.png";

        String publicUrl = cloudinaryClient.getUrl(AccessType.PUBLIC, fileName);
        log.info("The Public Url is : {}", publicUrl);
        assertNotNull(publicUrl);
    }

    @Test
    @DisplayName("Test to generate download url for private")
    @Disabled("Due to hitting Cloudinary")
    void testGeneratePrivateUrl() {
        String fileName = "Yubraj-Resume.pdf";

        String privateUrl = cloudinaryClient.getUrl(AccessType.PRIVATE, fileName);
        log.info("The Private Url is : {}", privateUrl);
        assertNotNull(privateUrl);
    }

    @Test
    @DisplayName("Test to delete public file")
    @Disabled("Due to hitting Cloudinary")
    void testDeletePublic() {
        String fileName = "logo.png";

        cloudinaryClient.delete(AccessType.PUBLIC, fileName);
    }

    @Test
    @DisplayName("Test to delete private file")
    @Disabled("Due to hitting Cloudinary")
    void testDeletePrivate() {
        String fileName = "Yubraj-Resume.pdf";

        cloudinaryClient.delete(AccessType.PRIVATE, fileName);
    }
}
