package com.eviive.personalapi.core.properties;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

import java.util.List;

@ConfigurationProperties(prefix = "personal-api.cors")
@Validated
public record CorsPropertiesConfig(
    @NotNull(message = "The CORS allowed origins must be specified.")
    @NotEmpty(message = "The CORS allowed origins must not be empty.")
    List<String> allowedOrigins
) {

}
