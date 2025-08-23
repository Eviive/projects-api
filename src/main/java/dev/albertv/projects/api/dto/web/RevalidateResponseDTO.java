package dev.albertv.projects.api.dto.web;

import java.time.LocalDateTime;

public record RevalidateResponseDTO(
    Boolean revalidated,
    LocalDateTime timestamp
) {

}
