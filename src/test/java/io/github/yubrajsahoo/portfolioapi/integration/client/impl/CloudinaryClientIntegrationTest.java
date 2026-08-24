package io.github.yubrajsahoo.portfolioapi.integration.client.impl;

import io.github.yubrajsahoo.portfolioapi.DataBuilderUtils;
import io.github.yubrajsahoo.portfolioapi.client.impl.CloudinaryClient;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.InputStream;

import static org.junit.jupiter.api.Assertions.assertNotNull;

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

        String publicId = cloudinaryClient.uploadPublic(inputStream, fileName);
        System.out.println("Public ID:" + publicId);
        assertNotNull(publicId);
    }

    @Test
    @DisplayName("Test upload file in private")
    @Disabled("Due to storing data in Cloudinary")
    void testUploadPrivate() {
        InputStream inputStream = DataBuilderUtils.readFile("src/test/resources/pdf/Yubraj-Resume.pdf");
        String fileName = "Yubraj-Resume.pdf";

        String publicId = cloudinaryClient.uploadPrivate(inputStream, fileName);
        System.out.println("Public ID:" + publicId);
        assertNotNull(publicId);
    }

    @Test
    @DisplayName("Test to generate download url for private")
    @Disabled("Due to hitting Cloudinary")
    void testGeneratePrivateUrl() {
        String fileName = "Yubraj-Resume.pdf";

        String privateUrl = cloudinaryClient.generatePrivateUrl(fileName);
        System.out.println("The Private Url is : " + privateUrl);
        assertNotNull(privateUrl);
    }

    @Test
    @DisplayName("Test to delete public file")
    @Disabled("Due to hitting Cloudinary")
    void testDeletePublic() {
        String fileName = "logo.png";

        cloudinaryClient.deletePublic(fileName);
    }

    @Test
    @DisplayName("Test to delete private file")
    @Disabled("Due to hitting Cloudinary")
    void testDeletePrivate() {
        String fileName = "Yubraj-Resume.pdf";

        cloudinaryClient.deletePrivate(fileName);
    }
}
