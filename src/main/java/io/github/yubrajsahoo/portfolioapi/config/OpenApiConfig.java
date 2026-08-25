/*
 *
 *  * Copyright (c) 2026 Yubraj Sahoo. All rights reserved.
 *
 */

package io.github.yubrajsahoo.portfolioapi.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Configuration class for OpenApi
 *
 * @author Yubraj Sahoo
 * @since 0.0.1-SNAPSHOT
 */
@Configuration
public class OpenApiConfig {

    /**
     * Create Bean for OpenApi
     *
     * @return the open api bean
     */
    @Bean
    public OpenAPI portfolioApiOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Portfolio API")
                        .description("Portfolio API documentation")
                        .version("0.0.1-SNAPSHOT")
                        .contact(new Contact()
                                .name("Yubraj Sahoo")
                        )
                );
    }
}
