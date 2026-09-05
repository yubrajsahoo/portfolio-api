package io.github.yubrajsahoo.portfolioapi.config;

import com.github.benmanes.caffeine.cache.Caffeine;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.caffeine.CaffeineCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.TimeUnit;

/**
 * Configuration class for setting up application-wide caching using Caffeine.
 *
 * <p>This configuration enables Spring's caching abstraction and registers a
 * {@link CacheManager} backed by Caffeine. The cache settings such as time-to-live (TTL)
 * and maximum size can be configured externally via application properties.</p>
 *
 * @author Yubraj Sahoo
 * @since 0.0.1-SNAPSHOT
 */
@Configuration
@EnableCaching
public class CacheConfig {

    /**
     * The Time-To-Live (TTL) for cache entries in minutes.
     * Defaults to 8 minutes if not specified.
     */
    @Value("${spring.cache.ttl:8}")
    private long cacheTtl;

    /**
     * The maximum number of entries the cache can hold.
     * Defaults to 1000 entries if not specified.
     */
    @Value("${spring.cache.max:1000}")
    private long cacheMax;

    /**
     * Creates a {@link Caffeine} builder bean pre-configured with the application's
     * expiration time and maximum size limits.
     *
     * @return a configured {@link Caffeine} builder instance
     */
    @Bean
    public Caffeine<Object, Object> caffeineConfig() {
        return Caffeine.newBuilder()
                .expireAfterWrite(cacheTtl, TimeUnit.MINUTES)
                .maximumSize(cacheMax);
    }

    /**
     * Creates the Spring {@link CacheManager} bean backed by the configured
     * Caffeine cache builder.
     *
     * @param caffeine the configured Caffeine builder
     * @return a {@link CaffeineCacheManager} instance
     */
    @Bean
    public CacheManager cacheManager(Caffeine<Object, Object> caffeine) {
        CaffeineCacheManager caffeineCacheManager = new CaffeineCacheManager();
        caffeineCacheManager.setCaffeine(caffeine);
        return caffeineCacheManager;
    }
}
