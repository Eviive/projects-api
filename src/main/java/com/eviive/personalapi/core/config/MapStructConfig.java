package com.eviive.personalapi.core.config;

import org.mapstruct.MapperConfig;

import static org.mapstruct.InjectionStrategy.CONSTRUCTOR;
import static org.mapstruct.MappingConstants.ComponentModel.SPRING;
import static org.mapstruct.ReportingPolicy.ERROR;

@MapperConfig(
    unmappedTargetPolicy = ERROR,
    componentModel = SPRING,
    injectionStrategy = CONSTRUCTOR
)
public interface MapStructConfig {

}
