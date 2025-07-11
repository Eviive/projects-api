package com.eviive.personalapi.dto;

public record ProjectLightDTO(
    Long id,
    String title,
    Boolean featured,
    Integer sort
) {

}
