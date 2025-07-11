package com.eviive.personalapi.dto;

import java.util.Set;

public record CurrentUserDTO(
    Long id,
    String username,
    String firstName,
    String lastName,
    Set<String> authorities
) {

}
