package com.eviive.personalapi.entity;

import jakarta.annotation.Nullable;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.List;
import java.util.Set;
import java.util.stream.Stream;

import static com.eviive.personalapi.entity.Scope.*;

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
            REVALIDATE_PORTFOLIO,
            READ_ACTUATOR
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
