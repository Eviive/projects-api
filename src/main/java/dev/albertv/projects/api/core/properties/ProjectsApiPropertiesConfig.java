package dev.albertv.projects.api.core.properties;

import jakarta.validation.constraints.NotNull;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

@ConfigurationProperties(prefix = "projects-api")
@Validated
public record ProjectsApiPropertiesConfig(
    @NotNull(message = "The production flag must be specified.")
    Boolean production
) {

}
