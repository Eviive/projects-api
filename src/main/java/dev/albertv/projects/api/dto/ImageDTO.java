package dev.albertv.projects.api.dto;

import jakarta.validation.constraints.NotBlank;
import org.hibernate.validator.constraints.Length;

import java.util.UUID;

public record ImageDTO(

    Long id,

    UUID uuid,

    @NotBlank(message = "The image's english alt cannot be blank.")
    @Length(max = 255, message = "The image's english alt cannot be longer than {max} characters.")
    String altEn,

    @NotBlank(message = "The image's french alt cannot be blank.")
    @Length(max = 255, message = "The image's french alt cannot be longer than {max} characters.")
    String altFr

) {

}
