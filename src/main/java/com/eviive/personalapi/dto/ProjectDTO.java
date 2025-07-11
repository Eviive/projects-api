package com.eviive.personalapi.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.hibernate.validator.constraints.Length;

import java.time.LocalDate;
import java.util.Set;

public record ProjectDTO(

    Long id,

    @NotBlank(message = "The project's title cannot be blank.")
    @Length(max = 50, message = "The project's title cannot be longer than {max} characters.")
    String title,

    @NotBlank(message = "The project's english description cannot be blank.")
    @Length(max = 510, message = "The project's english description cannot be longer than {max} characters.")
    String descriptionEn,

    @NotBlank(message = "The project's french description cannot be blank.")
    @Length(max = 510, message = "The project's french description cannot be longer than {max} characters.")
    String descriptionFr,

    @NotNull(message = "The project's creation date is required.")
    LocalDate creationDate,

    @NotBlank(message = "The project's repository URL cannot be blank.")
    @Length(max = 255, message = "The project's repository URL cannot be longer than {max} characters.")
    String repoUrl,

    @NotBlank(message = "The project's demo URL cannot be blank.")
    @Length(max = 255, message = "The project's demo URL cannot be longer than {max} characters.")
    String demoUrl,

    @NotNull(message = "The project's featured status is required.")
    Boolean featured,

    @Min(value = 0, message = "The project's sort cannot be less than {min}.")
    Integer sort,

    @NotNull(message = "The project's image is required.")
    @Valid
    ImageDTO image,

    @NotNull(message = "The project's skills are required.")
    Set<@Valid SkillDTO> skills

) {

}
