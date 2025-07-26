package dev.albertv.projects.api.dto;

import java.util.Set;

public record CurrentUserDTO(
    String username,
    String email,
    String name,
    String avatar,
    Set<String> authorities
) {

}
