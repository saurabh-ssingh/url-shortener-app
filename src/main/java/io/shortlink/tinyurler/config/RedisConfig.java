package io.shortlink.tinyurler.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext;

import java.time.Duration;

/**
 * RedisConfig defines the caching configuration for Redis in the application.
 * <p>
 * This configuration sets:
 * - A default TTL (time-to-live) of 30 minutes for cached entries.
 * - Disables caching of null values.
 * - Uses JSON serialization for values, making cached data human-readable.
 * </p>
 */
@Configuration
public class RedisConfig {

    /**
     * Configures the default cache behavior for Redis.
     *
     * @return RedisCacheConfiguration instance with custom settings.
     */
    @Bean
    public RedisCacheConfiguration cacheConfiguration() {
        return RedisCacheConfiguration.defaultCacheConfig()
                // Set TTL for each cache entry to 30 minutes
                .entryTtl(Duration.ofMinutes(30))
                // Avoid storing null values in cache
                .disableCachingNullValues()
                // Serialize values into JSON (for readability and portability)
                .serializeValuesWith(
                        RedisSerializationContext.SerializationPair.fromSerializer(
                                new GenericJackson2JsonRedisSerializer()));
    }
}
