package com.eviive.personalapi.dto.web;

import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;

public record RevalidateResponseDTO(

    @NotNull(message = "The revalidated status is required.")
    Boolean revalidated,

    @NotNull(message = "The timestamp is required.")
    LocalDateTime timestamp

) {

}
