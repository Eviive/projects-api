package com.eviive.personalapi.mapper;

import com.eviive.personalapi.core.config.MapStructConfig;
import com.eviive.personalapi.dto.CurrentUserDTO;
import com.eviive.personalapi.entity.User;
import org.jetbrains.annotations.Nullable;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;

@Mapper(config = MapStructConfig.class)
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
