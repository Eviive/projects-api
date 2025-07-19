package dev.albertv.projects.api.mapper;

import dev.albertv.projects.api.core.config.MapStructConfig;
import dev.albertv.projects.api.dto.CurrentUserDTO;
import dev.albertv.projects.api.entity.User;
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
