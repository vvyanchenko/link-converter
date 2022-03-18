package link.converter.config;

import org.springframework.cache.CacheManager;
import org.springframework.cache.interceptor.CacheOperationInvocationContext;
import org.springframework.cache.interceptor.SimpleCacheResolver;
import org.springframework.core.env.PropertyResolver;

import java.util.Collection;
import java.util.stream.Collectors;

/**
 * Class required to resolve cache names from application properties
 */
public class PropertyResolvingCacheResolver extends SimpleCacheResolver {

    private final PropertyResolver propertyResolver;

    public PropertyResolvingCacheResolver(CacheManager cacheManager, PropertyResolver propertyResolver) {
        super(cacheManager);
        this.propertyResolver = propertyResolver;
    }

    @Override
    protected Collection<String> getCacheNames(CacheOperationInvocationContext<?> context) {
        Collection<String> unresolvedCacheNames = super.getCacheNames(context);
        return unresolvedCacheNames.stream()
                .map(propertyResolver::resolveRequiredPlaceholders)
                .collect(Collectors.toList());
    }

}
