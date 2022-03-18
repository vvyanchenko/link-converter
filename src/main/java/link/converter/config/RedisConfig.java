package link.converter.config;

import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisClusterConfiguration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;

import java.util.Collections;
import java.util.List;

/**
 * Configuration class required to define Redis-specific configurations
 */
@Configuration
@EnableCaching
public class RedisConfig {

    @Bean
    @Profile("aws")
    public RedisConnectionFactory redisConnectionFactory(RedisClusterConfigProperties redisClusterConfigProperties) {
        List<String> nodes = Collections.singletonList(redisClusterConfigProperties.getNode());
        RedisClusterConfiguration configuration = new RedisClusterConfiguration(nodes);
        return new LettuceConnectionFactory(configuration);
    }

    @Bean
    public RedisCacheManager redisCacheManager(RedisConnectionFactory connectionFactory) {
        return RedisCacheManager.create(connectionFactory);
    }

}
