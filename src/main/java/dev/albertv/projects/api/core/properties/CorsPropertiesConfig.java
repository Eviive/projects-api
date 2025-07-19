package dev.albertv.projects.api.core.properties;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.cors.CorsConfiguration;

import java.util.List;

@ConfigurationProperties(prefix = "projects-api.cors")
@Validated
@AllArgsConstructor
public final class CorsPropertiesConfig {

    @NotNull(message = "The CORS allowed origins must be specified.")
    @NotEmpty(message = "The CORS allowed origins must not be empty.")
    private final List<String> allowedOrigins;

    @NotNull(message = "The CORS allowed methods must be specified.")
    @NotEmpty(message = "The CORS allowed methods must not be empty.")
    private final List<String> allowedMethods;

    @NotNull(message = "The CORS allowed headers must be specified.")
    @NotEmpty(message = "The CORS allowed headers must not be empty.")
    private final List<String> allowedHeaders;

    @NotNull(message = "The CORS exposed headers must be specified.")
    @NotEmpty(message = "The CORS exposed headers must not be empty.")
    private final List<String> exposedHeaders;

    @NotNull(message = "The CORS allow credentials must be specified.")
    private final Boolean allowCredentials;

    @NotNull(message = "The CORS max age must be specified.")
    @Positive(message = "The CORS max age must be positive.")
    private final Long maxAge;

    public CorsConfiguration getConfiguration() {
        final CorsConfiguration corsConfiguration = new CorsConfiguration();
        corsConfiguration.setAllowedOriginPatterns(allowedOrigins);
        corsConfiguration.setAllowedMethods(allowedMethods);
        corsConfiguration.setAllowedHeaders(allowedHeaders);
        corsConfiguration.setExposedHeaders(exposedHeaders);
        corsConfiguration.setAllowCredentials(allowCredentials);
        corsConfiguration.setMaxAge(maxAge);
        return corsConfiguration;
    }

}
