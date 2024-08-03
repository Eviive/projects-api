package com.eviive.personalapi.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.hibernate.validator.constraints.Length;

import java.util.Set;

public record CurrentUserDTO(

    Long id,

    @NotBlank(message = "The user's name cannot be blank.")
    @Length(max = 50, message = "The user's name cannot be longer than {max} characters.")
    String username,

    @NotBlank(message = "The user's first name cannot be blank.")
    @Length(max = 50, message = "The user's first name cannot be longer than {max} characters.")
    String firstName,

    @NotBlank(message = "The user's last name cannot be blank.")
    @Length(max = 50, message = "The user's last name cannot be longer than {max} characters.")
    String lastName,

    @NotNull(message = "The user's authorities are required.")
    Set<String> authorities

) {

    public CurrentUserDTO {
        if (username == null) {
            username = "Guest";
        }
    }

}
