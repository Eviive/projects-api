package com.eviive.personalapi.dto.web;

public record RevalidateRequestDTO(
    String secret,
    String path
) {

}
