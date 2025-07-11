package com.eviive.personalapi.dto.web;

import java.time.LocalDateTime;

public record RevalidateResponseDTO(
    Boolean revalidated,
    LocalDateTime timestamp
) {

}
