package dev.albertv.projects.api.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public record SortUpdateDTO(

    @NotNull(message = "The sort id is required.")
    Long id,

    @NotNull(message = "The sort value is required.")
    @Min(value = 0, message = "The sort value cannot be less than {min}.")
    Integer sort

) {

}
