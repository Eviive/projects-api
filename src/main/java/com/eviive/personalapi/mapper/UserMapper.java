package com.eviive.personalapi.mapper;

import com.eviive.personalapi.dto.CurrentUserDTO;
import com.eviive.personalapi.entity.User;
import jakarta.annotation.Nullable;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;

import static org.mapstruct.InjectionStrategy.CONSTRUCTOR;
import static org.mapstruct.MappingConstants.ComponentModel.SPRING;
import static org.mapstruct.ReportingPolicy.ERROR;

@Mapper(
    unmappedTargetPolicy = ERROR,
    componentModel = SPRING,
    injectionStrategy = CONSTRUCTOR
)
public interface UserMapper {

    // to Current DTO

    @Mapping(target = "id", source = "user.id")
    @Mapping(target = "username", source = "user.username")
    @Mapping(target = "authorities", source = "authorities")
    CurrentUserDTO toCurrentDTO(@Nullable User user, Collection<? extends GrantedAuthority> authorities);

    default String mapAuthority(GrantedAuthority authority) {
        return authority.getAuthority();
    }

}
