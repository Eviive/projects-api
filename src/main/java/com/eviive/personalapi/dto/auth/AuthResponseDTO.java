package com.eviive.personalapi.dto.auth;

import com.eviive.personalapi.dto.CurrentUserDTO;

public record AuthResponseDTO(
    CurrentUserDTO currentUser,
    String accessToken
) {

}
