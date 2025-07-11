package com.eviive.personalapi.core.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import io.swagger.v3.oas.annotations.servers.Server;

import static io.swagger.v3.oas.annotations.enums.SecuritySchemeIn.HEADER;
import static io.swagger.v3.oas.annotations.enums.SecuritySchemeType.HTTP;

@OpenAPIDefinition(
    info = @Info(
        title = "OpenAPI documentation - Personal-API",
        contact = @Contact(
            name = "Albert VAILLON",
            url = "https://albertv.dev",
            email = "albert.vaillon21@gmail.com"
        ),
        version = "${info.app.version}"
    ),
    servers = {
        @Server(description = "Local environment", url = "http://localhost:8080"),
        @Server(description = "Production environment", url = "https://api.albertv.dev")
    },
    security = {
        @SecurityRequirement(name = "Bearer Authentication")
    }
)
@SecurityScheme(
    type = HTTP,
    name = "Bearer Authentication",
    description = "JWT authentication for the Personal-API",
    in = HEADER,
    scheme = "bearer",
    bearerFormat = "JWT"
)
public class OpenAPIConfig {

}
