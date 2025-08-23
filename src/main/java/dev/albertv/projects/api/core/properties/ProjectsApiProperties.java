package dev.albertv.projects.api.core.properties;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

@ConfigurationProperties(prefix = "projects-api")
@Validated
public record ProjectsApiProperties(
    @NotNull(message = "The production flag must be specified.")
    Boolean production,
    @NotBlank(message = "The version must be specified.")
    String version,
    @NotBlank(message = "The stage must be specified.")
    String stage
) {

}
