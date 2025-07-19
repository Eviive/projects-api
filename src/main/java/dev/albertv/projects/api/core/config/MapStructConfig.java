package dev.albertv.projects.api.core.config;

import org.mapstruct.MapperConfig;

import static org.mapstruct.InjectionStrategy.CONSTRUCTOR;
import static org.mapstruct.MappingConstants.ComponentModel.SPRING;
import static org.mapstruct.NullValueMappingStrategy.RETURN_DEFAULT;
import static org.mapstruct.NullValueMappingStrategy.RETURN_NULL;
import static org.mapstruct.NullValuePropertyMappingStrategy.SET_TO_NULL;
import static org.mapstruct.ReportingPolicy.ERROR;
import static org.mapstruct.ReportingPolicy.IGNORE;

@MapperConfig(
    unmappedTargetPolicy = ERROR,
    typeConversionPolicy = ERROR,
    unmappedSourcePolicy = IGNORE,
    componentModel = SPRING,
    nullValueMappingStrategy = RETURN_NULL,
    nullValueIterableMappingStrategy = RETURN_DEFAULT,
    nullValueMapMappingStrategy = RETURN_DEFAULT,
    nullValuePropertyMappingStrategy = SET_TO_NULL,
    injectionStrategy = CONSTRUCTOR
)
public interface MapStructConfig {

}
