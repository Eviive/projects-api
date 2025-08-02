package dev.albertv.projects.api.dto.web;

public record RevalidateRequestDTO(
    String secret,
    String path
) {

}
