package link.converter.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotBlank;

/**
 * Configuration properties used to externalize domain specific URL requirements to be used in business logic e.g. validation
 */
@Getter
@Setter
@Validated
@Component
@ConfigurationProperties(prefix = "trendyol.url")
public class UrlConfigProperties {

    @NotBlank
    private String hostname;

    @NotBlank
    private String protocol;

    @NotBlank
    private String homepage;

}
