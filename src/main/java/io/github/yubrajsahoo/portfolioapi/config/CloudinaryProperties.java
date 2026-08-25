/*
 *
 *  * Copyright (c) 2026 Yubraj Sahoo. All rights reserved.
 *
 */

package io.github.yubrajsahoo.portfolioapi.config;

import jakarta.validation.constraints.NotBlank;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

import java.time.Duration;
import java.util.Map;

/**
 * Configuration properties for Cloudinary.
 *
 * @param cloudName     cloud name
 * @param apiKey        api key
 * @param apiSecret     api secret
 * @param privateUrlTtl time-to-live duration for private signed URLs (defaults to 5 minutes)
 * @author Yubraj Sahoo
 * @since 0.0.1-SNAPSHOT
 */
@Validated
@ConfigurationProperties(prefix = "cloudinary")
public record CloudinaryProperties(
        /*
          cloud name of cloudinary
         */
        @NotBlank(message = "Cloudinary cloud name is required")
        String cloudName,

        /*
          api key for cloudinary
         */
        @NotBlank(message = "Cloudinary api key is required")
        String apiKey,

        /*
          api secret for cloudinary
         */
        @NotBlank(message = "Cloudinary api secret is required")
        String apiSecret,

        /*
          validity duration for private download URLs
         */
        Duration privateUrlTtl
) {
    public CloudinaryProperties {
        if (privateUrlTtl == null) {
            privateUrlTtl = Duration.ofMinutes(5);
        }
    }

    /**
     * Creates the Cloudinary configuration map.
     *
     * @return Cloudinary configuration
     */
    public Map<String, String> toMap() {
        return Map.of(
                "cloud_name", cloudName,
                "api_key", apiKey,
                "api_secret", apiSecret
        );
    }
}

