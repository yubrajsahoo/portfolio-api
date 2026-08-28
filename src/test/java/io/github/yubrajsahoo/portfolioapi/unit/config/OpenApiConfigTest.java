package io.github.yubrajsahoo.portfolioapi.unit.config;

import io.github.yubrajsahoo.portfolioapi.config.OpenApiConfig;
import io.swagger.v3.oas.models.OpenAPI;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertEquals;

@DisplayName("Unit: OpenApi Config")
class OpenApiConfigTest {

    @Test
    @DisplayName("Should Create OpenAPI Bean")
    void openApiBean() {
        OpenApiConfig config = new OpenApiConfig();
        OpenAPI openAPI = config.portfolioApiOpenAPI();
        
        assertNotNull(openAPI);
        assertNotNull(openAPI.getInfo());
        assertEquals("Portfolio API", openAPI.getInfo().getTitle());
        assertEquals("Portfolio API documentation", openAPI.getInfo().getDescription());
        assertEquals("0.0.1-SNAPSHOT", openAPI.getInfo().getVersion());
        assertEquals("Yubraj Sahoo", openAPI.getInfo().getContact().getName());
    }
}
