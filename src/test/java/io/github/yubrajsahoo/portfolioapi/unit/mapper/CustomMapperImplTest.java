package io.github.yubrajsahoo.portfolioapi.unit.mapper;

import io.github.yubrajsahoo.portfolioapi.domain.FileMetaData;
import io.github.yubrajsahoo.portfolioapi.dto.CloudFileDto;
import io.github.yubrajsahoo.portfolioapi.enums.AccessType;
import io.github.yubrajsahoo.portfolioapi.helper.DataBuilderUtils;
import io.github.yubrajsahoo.portfolioapi.mapper.impl.CustomMapperImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
@DisplayName("Unit: Custom Mapper Transformations")
class CustomMapperImplTest {

    @Autowired
    private CustomMapperImpl customMapper;

    @Test
    @DisplayName("Should Map Public File Data to Correct Metadata")
    void toFileMetaData_Public() {
        FileMetaData expectedMetaData = DataBuilderUtils.readFromJson(
                "src/test/resources/json/file-meta-data-public-png-logo.json",
                FileMetaData.class
        );

        FileMetaData result = customMapper.toFileMetaData("logo.png", AccessType.PUBLIC);

        assertNotNull(result);
        assertEquals(expectedMetaData.getFileName(), result.getFileName());
        assertEquals(expectedMetaData.getExtension(), result.getExtension());
        assertEquals(expectedMetaData.getFolder(), result.getFolder());
        assertEquals(expectedMetaData.getAccessType(), result.getAccessType());
        assertEquals(expectedMetaData.getResourceType(), result.getResourceType());
    }

    @Test
    @DisplayName("Should Map Private File Data to Correct Metadata")
    void toFileMetaData_Private() {
        FileMetaData expectedMetaData = DataBuilderUtils.readFromJson(
                "src/test/resources/json/file-meta-data-private-pdf-resume.json",
                FileMetaData.class
        );

        FileMetaData result = customMapper.toFileMetaData("Yubraj-Resume.pdf", AccessType.PRIVATE);

        assertNotNull(result);
        assertEquals(expectedMetaData.getFileName(), result.getFileName());
        assertEquals(expectedMetaData.getExtension(), result.getExtension());
        assertEquals(expectedMetaData.getFolder(), result.getFolder());
        assertEquals(expectedMetaData.getAccessType(), result.getAccessType());
        assertEquals(expectedMetaData.getResourceType(), result.getResourceType());
    }

    @Test
    @DisplayName("Should Throw Exception for Invalid File Name")
    void toFileMetaData_InvalidFileName() {
        assertThrows(IllegalArgumentException.class, () -> customMapper.toFileMetaData("test", AccessType.PUBLIC));
    }

    @Test
    @DisplayName("Should Map Cloudinary URL to CloudFileDto Successfully")
    void toCloudFileDto_Public() {
        String mockUrl = "https://res.cloudinary.com/demo/image/upload/v1/portfolio/public/image/logo.png";
        CloudFileDto result = customMapper.toCloudFileDto(mockUrl);

        assertNotNull(result);
        assertEquals("logo.png", result.getFileName());
        assertEquals(AccessType.PUBLIC, result.getAccess());
        assertEquals("portfolio", result.getProject());
    }
    
    @Test
    @DisplayName("Should Return Null for Invalid Cloudinary URL")
    void toCloudFileDto_InvalidUrl() {
        String mockUrl = "invalid_url_with_no_slashes";
        CloudFileDto result = customMapper.toCloudFileDto(mockUrl);
        org.junit.jupiter.api.Assertions.assertNull(result);
    }
}
