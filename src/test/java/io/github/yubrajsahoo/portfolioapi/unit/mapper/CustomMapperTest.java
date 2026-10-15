package io.github.yubrajsahoo.portfolioapi.unit.mapper;

import io.github.yubrajsahoo.portfolioapi.domain.FileMetaData;
import io.github.yubrajsahoo.portfolioapi.dto.CloudFileDto;
import io.github.yubrajsahoo.portfolioapi.enums.AccessType;
import io.github.yubrajsahoo.portfolioapi.helper.DataBuilderUtils;
import io.github.yubrajsahoo.portfolioapi.mapper.impl.CustomMapperImpl;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@DisplayName("Unit: Custom Mapper Transformations")
@ActiveProfiles("unit")
class CustomMapperTest {

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
        assertEquals("portfolio", result.getApplication());
    }
    
    @Test
    @DisplayName("Should Return Null for Invalid Cloudinary URL")
    void toCloudFileDto_InvalidUrl() {
        String mockUrl = "invalid_url_with_no_slashes";
        CloudFileDto result = customMapper.toCloudFileDto(mockUrl);
        org.junit.jupiter.api.Assertions.assertNull(result);
    }

    @Test
    @DisplayName("Should Map UserRegistrationDto to User Entity")
    void toUserEntity() {
        io.github.yubrajsahoo.portfolioapi.dto.UserReqDto dto = new io.github.yubrajsahoo.portfolioapi.dto.UserReqDto();
        dto.setEmail("test@test.com");
        dto.setPassword("password");

        io.github.yubrajsahoo.portfolioapi.entity.Role role = new io.github.yubrajsahoo.portfolioapi.entity.Role();
        role.setName("USER");
        java.util.List<io.github.yubrajsahoo.portfolioapi.entity.Role> roles = java.util.List.of(role);

        io.github.yubrajsahoo.portfolioapi.entity.User user = customMapper.toUserEntity(dto, roles);

        assertNotNull(user);
        assertEquals("test@test.com", user.getEmail());
        assertEquals("password", user.getPassword());
        assertEquals(1, user.getRoles().size());
        assertEquals("USER", user.getRoles().get(0).getName());
    }

    @Test
    @DisplayName("Should Map ContactReqDto to ContactMessage Entity")
    void toContactMessageEntity() {
        io.github.yubrajsahoo.portfolioapi.dto.ContactReqDto dto = io.github.yubrajsahoo.portfolioapi.dto.ContactReqDto.builder()
                .name("Alice")
                .email("alice@example.com")
                .message("Hello there")
                .build();

        io.github.yubrajsahoo.portfolioapi.entity.ContactMessage message = customMapper.toContactMessageEntity(dto);

        assertNotNull(message);
        assertEquals("Alice", message.getName());
        assertEquals("alice@example.com", message.getEmail());
        assertEquals("Hello there", message.getMessage());

        org.junit.jupiter.api.Assertions.assertNull(customMapper.toContactMessageEntity(null));
    }

    @Test
    @DisplayName("Should Map ContactMessage Entity to ContactMessageDto")
    void toContactMessageDto() {
        io.github.yubrajsahoo.portfolioapi.entity.ContactMessage entity = io.github.yubrajsahoo.portfolioapi.entity.ContactMessage.builder()
                .id(5L)
                .name("Bob")
                .email("bob@example.com")
                .message("Test inquiry")
                .isRead(true)
                .build();

        io.github.yubrajsahoo.portfolioapi.dto.ContactMessageDto dto = customMapper.toContactMessageDto(entity);

        assertNotNull(dto);
        assertEquals(5L, dto.getId());
        assertEquals("Bob", dto.getName());
        assertEquals("bob@example.com", dto.getEmail());
        assertEquals("Test inquiry", dto.getMessage());
        org.junit.jupiter.api.Assertions.assertTrue(dto.isRead());

        org.junit.jupiter.api.Assertions.assertNull(customMapper.toContactMessageDto(null));
    }
}
