package dev.albertv.projects.api.core.properties;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

@ConfigurationProperties(prefix = "projects-api.portfolio")
@Validated
public record PortfolioPropertiesConfig(
    @NotNull(message = "The portfolio API configuration must be specified.")
    Api api
) {

    public record Api(
        @NotBlank(message = "The portfolio API URL must be specified.")
        String url,
        @NotBlank(message = "The portfolio API secret must be specified.")
        String secret
    ) {

    }

}
