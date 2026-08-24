package io.github.yubrajsahoo.portfolioapi.config;

import com.cloudinary.Cloudinary;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Configuration class for Cloudinary.
 *
 * @author Yubraj Sahoo
 * @since 0.0.1-SNAPSHOT
 */
@Configuration
@EnableConfigurationProperties(CloudinaryProperties.class)
public class CloudinaryConfig {

    /**
     * Creates the cloudinary bean
     *
     * @param properties the cloudinary properites
     * @return the Cloudinary client
     */
    @Bean
    public Cloudinary cloudinary(CloudinaryProperties properties) {
        return new Cloudinary(properties.toMap());
    }
}
