package com.eviive.personalapi.core.properties;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

@ConfigurationProperties(prefix = "personal-api.jwt")
@Validated
public record JwtPropertiesConfig(
    @NotBlank(message = "The JWT secret must be specified.")
    String secret,
    @NotNull(message = "The JWT tokens configuration must be specified.")
    Tokens token
) {

    public record Tokens(
        @NotNull(message = "The JWT access token configuration must be specified.")
        Token access,
        @NotNull(message = "The JWT refresh token configuration must be specified.")
        Token refresh
    ) {

        public record Token(
            @NotNull(message = "The JWT token's time before expiration must be specified.")
            @Positive(message = "The JWT token's time before expiration must be positive.")
            Integer expiration
        ) {

        }

    }

}
