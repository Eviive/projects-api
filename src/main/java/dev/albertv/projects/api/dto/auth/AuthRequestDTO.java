package dev.albertv.projects.api.dto.auth;

import jakarta.validation.constraints.NotBlank;

public record AuthRequestDTO(

    @NotBlank(message = "The username cannot be blank.")
    String username,

    @NotBlank(message = "The password cannot be blank.")
    String password

) {

}
