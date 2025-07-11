package com.eviive.personalapi.core.properties;

import jakarta.validation.constraints.NotNull;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

@ConfigurationProperties(prefix = "personal-api")
@Validated
public record PersonalApiPropertiesConfig(
    @NotNull(message = "The production flag must be specified.")
    Boolean production
) {

}
