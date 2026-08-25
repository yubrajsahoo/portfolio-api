package io.github.yubrajsahoo.portfolioapi.integration.client.impl;

import io.github.yubrajsahoo.portfolioapi.DataBuilderUtils;
import io.github.yubrajsahoo.portfolioapi.client.impl.CloudinaryClient;
import io.github.yubrajsahoo.portfolioapi.domain.FileMetaData;
import io.github.yubrajsahoo.portfolioapi.enums.AccessType;
import io.github.yubrajsahoo.portfolioapi.enums.ResourceType;
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
        FileMetaData metaData = FileMetaData.builder()
                .fileName("logo")
                .extension("png")
                .folder("portfolio/public/image")
                .accessType(AccessType.PUBLIC)
                .resourceType(ResourceType.IMAGE)
                .build();

        String publicId = cloudinaryClient.upload(inputStream, metaData);
        log.info("Public File ID:{}", publicId);
        assertNotNull(publicId);
    }

    @Test
    @DisplayName("Test upload file in private")
    @Disabled("Due to storing data in Cloudinary")
    void testUploadPrivate() {
        InputStream inputStream = DataBuilderUtils.readFile("src/test/resources/pdf/Yubraj-Resume.pdf");
        FileMetaData metaData = FileMetaData.builder()
                .fileName("Yubraj-Resume")
                .extension("pdf")
                .folder("portfolio/private/raw")
                .accessType(AccessType.PRIVATE)
                .resourceType(ResourceType.RAW)
                .build();

        String publicId = cloudinaryClient.upload(inputStream, metaData);
        log.info("Private File ID:{}", publicId);
        assertNotNull(publicId);
    }

    @Test
    @DisplayName("Test to generate download url for public")
    void testGeneratePublicUrl() {
        FileMetaData metaData = FileMetaData.builder()
                .fileName("logo")
                .extension("png")
                .folder("portfolio/public/image")
                .accessType(AccessType.PUBLIC)
                .resourceType(ResourceType.IMAGE)
                .build();

        String publicUrl = cloudinaryClient.getUrl(metaData);
        log.info("The Public Url is : {}", publicUrl);
        assertNotNull(publicUrl);
    }

    @Test
    @DisplayName("Test to generate download url for private")
    @Disabled("Due to hitting Cloudinary")
    void testGeneratePrivateUrl() {
        FileMetaData metaData = FileMetaData.builder()
                .fileName("Yubraj-Resume")
                .extension("pdf")
                .folder("portfolio/private/raw")
                .accessType(AccessType.PRIVATE)
                .resourceType(ResourceType.RAW)
                .build();

        String privateUrl = cloudinaryClient.getUrl(metaData);
        log.info("The Private Url is : {}", privateUrl);
        assertNotNull(privateUrl);
    }

    @Test
    @DisplayName("Test to delete public file")
    @Disabled("Due to hitting Cloudinary")
    void testDeletePublic() {
        FileMetaData metaData = FileMetaData.builder()
                .fileName("logo")
                .extension("png")
                .folder("portfolio/public/image")
                .accessType(AccessType.PUBLIC)
                .resourceType(ResourceType.IMAGE)
                .build();

        cloudinaryClient.delete(metaData);
    }

    @Test
    @DisplayName("Test to delete private file")
    @Disabled("Due to hitting Cloudinary")
    void testDeletePrivate() {
        FileMetaData metaData = FileMetaData.builder()
                .fileName("Yubraj-Resume")
                .extension("pdf")
                .folder("portfolio/private/raw")
                .accessType(AccessType.PRIVATE)
                .resourceType(ResourceType.RAW)
                .build();

        cloudinaryClient.delete(metaData);
    }
}
