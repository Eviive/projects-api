package com.eviive.personalapi.dto.web;

import jakarta.validation.constraints.NotBlank;

public record RevalidateRequestDTO(

    @NotBlank(message = "The secret cannot be blank.")
    String secret,

    String path

) {

}
