package io.shortlink.tinyurler;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
public class RedisConnectionTest {

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    @Test
    void testRedisConnection() {
        String key = "test:key";
        String value = "HelloRedis";

        // Save to Redis
        redisTemplate.opsForValue().set(key, value);

        // Read from Redis
        String storedValue = (String) redisTemplate.opsForValue().get(key);

        assertThat(storedValue).isEqualTo(value);
    }
}
