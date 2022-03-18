package link.converter.config;

import lombok.AllArgsConstructor;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.CachingConfigurerSupport;
import org.springframework.cache.interceptor.CacheResolver;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;

/**
 * Configuration class required to define cache-specific configurations
 */
@Configuration
@AllArgsConstructor
public class CacheConfig extends CachingConfigurerSupport {

    private final Environment environment;
    private final CacheManager cacheManager;

    @Bean
    @Override
    public CacheResolver cacheResolver() {
        return new PropertyResolvingCacheResolver(cacheManager, environment);
    }

}
