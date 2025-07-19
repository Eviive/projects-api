package dev.albertv.projects.api.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.hibernate.validator.constraints.Length;

public record SkillDTO(

    Long id,

    @NotBlank(message = "The skill's name cannot be blank.")
    @Length(max = 50, message = "The skill's name cannot be longer than {max} characters.")
    String name,

    @Min(value = 0, message = "The skill's sort cannot be less than {min}.")
    Integer sort,

    @NotNull(message = "The skill's image is required.")
    @Valid
    ImageDTO image

) {

}
