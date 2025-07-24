package dev.albertv.projects.api.entity;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.Nullable;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.List;
import java.util.Set;
import java.util.stream.Stream;

import static dev.albertv.projects.api.entity.Scope.CREATE_PROJECT;
import static dev.albertv.projects.api.entity.Scope.CREATE_SKILL;
import static dev.albertv.projects.api.entity.Scope.DELETE_PROJECT;
import static dev.albertv.projects.api.entity.Scope.DELETE_SKILL;
import static dev.albertv.projects.api.entity.Scope.READ_PROJECT;
import static dev.albertv.projects.api.entity.Scope.READ_SKILL;
import static dev.albertv.projects.api.entity.Scope.REVALIDATE_PORTFOLIO;
import static dev.albertv.projects.api.entity.Scope.UPDATE_PROJECT;
import static dev.albertv.projects.api.entity.Scope.UPDATE_SKILL;

@RequiredArgsConstructor
public enum Role {

    ANONYMOUS(
        Set.of(
            READ_PROJECT,
            READ_SKILL
        ),
        null
    ),
    ADMIN(
        Set.of(
            CREATE_PROJECT, UPDATE_PROJECT, DELETE_PROJECT,
            CREATE_SKILL, UPDATE_SKILL, DELETE_SKILL,
            REVALIDATE_PORTFOLIO
        ),
        Set.of(
            ANONYMOUS
        )
    );

    private final Set<Scope> scopes;

    @Nullable
    @Getter
    private final Set<Role> implies;

    public List<GrantedAuthority> getAuthorities() {
        final Stream<GrantedAuthority> grantedAuthorities = Stream
            .concat(
                Stream.of(toString()),
                scopes.stream().map(Scope::getName)
            )
            .map(SimpleGrantedAuthority::new)
            .map(GrantedAuthority.class::cast);

        if (implies == null) {
            return grantedAuthorities.toList();
        }

        return Stream
            .concat(
                grantedAuthorities,
                implies.stream().flatMap(iR -> iR.getAuthorities().stream())
            )
            .toList();
    }

    @Override
    public String toString() {
        return "ROLE_" + name();
    }

}
