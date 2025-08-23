package dev.albertv.projects.api.core.config.security;

import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Stream;

import static dev.albertv.projects.api.core.config.security.Permission.CREATE_PROJECT;
import static dev.albertv.projects.api.core.config.security.Permission.CREATE_SKILL;
import static dev.albertv.projects.api.core.config.security.Permission.DELETE_PROJECT;
import static dev.albertv.projects.api.core.config.security.Permission.DELETE_SKILL;
import static dev.albertv.projects.api.core.config.security.Permission.READ_PROJECT;
import static dev.albertv.projects.api.core.config.security.Permission.READ_SKILL;
import static dev.albertv.projects.api.core.config.security.Permission.REVALIDATE_PORTFOLIO;
import static dev.albertv.projects.api.core.config.security.Permission.UPDATE_PROJECT;
import static dev.albertv.projects.api.core.config.security.Permission.UPDATE_SKILL;

public enum Role {

    ANONYMOUS(
        READ_PROJECT,
        READ_SKILL
    ),
    USER,
    ADMIN(
        CREATE_PROJECT, UPDATE_PROJECT, DELETE_PROJECT,
        CREATE_SKILL, UPDATE_SKILL, DELETE_SKILL,
        REVALIDATE_PORTFOLIO
    );

    static {
        USER.implies.add(ANONYMOUS);
        ADMIN.implies.add(USER);
    }

    private final Set<Permission> permissions;

    @Getter
    private final Set<Role> implies = new HashSet<>();

    Role(final Permission... permissions) {
        this.permissions = Set.of(permissions);
    }

    public List<GrantedAuthority> getPermissions() {
        final Stream<GrantedAuthority> grantedAuthorities = Stream
            .concat(
                Stream.of(toString()),
                permissions.stream().map(Permission::getName)
            )
            .map(SimpleGrantedAuthority::new)
            .map(GrantedAuthority.class::cast);

        return Stream
            .concat(
                grantedAuthorities,
                implies.stream().flatMap(iR -> iR.getPermissions().stream())
            )
            .toList();
    }

    @Override
    public String toString() {
        return "ROLE_" + name();
    }

}
