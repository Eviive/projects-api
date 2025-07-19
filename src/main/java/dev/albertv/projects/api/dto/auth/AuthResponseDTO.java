package dev.albertv.projects.api.dto.auth;

import dev.albertv.projects.api.dto.CurrentUserDTO;

public record AuthResponseDTO(
    CurrentUserDTO currentUser,
    String accessToken
) {

}
