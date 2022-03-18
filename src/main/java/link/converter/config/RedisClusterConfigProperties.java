package link.converter.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotBlank;

/**
 * Configuration properties used to define redis cluster master node for aws profile
 */
@Getter
@Setter
@Validated
@Component
@Profile("aws")
@ConfigurationProperties(prefix = "redis", ignoreUnknownFields = false)
public class RedisClusterConfigProperties {

    @NotBlank
    private String node;

}
