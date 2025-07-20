package dev.albertv.projects.api.dto;

import java.util.Set;

public record CurrentUserDTO(
    String username,
    String email,
    Set<String> authorities,
    Long exp
) {

    public static CurrentUserDTO anonymous() {
        return new CurrentUserDTO("Guest", null, Set.of(), null);
    }

}
