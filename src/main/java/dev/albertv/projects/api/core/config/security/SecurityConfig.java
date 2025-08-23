package dev.albertv.projects.api.core.config.security;

import com.c4_soft.springaddons.security.oidc.starter.ClaimSetAuthoritiesConverter;
import com.c4_soft.springaddons.security.oidc.starter.ConfigurableClaimSetAuthoritiesConverter;
import com.c4_soft.springaddons.security.oidc.starter.OpenidProviderPropertiesResolver;
import com.c4_soft.springaddons.security.oidc.starter.synchronised.resourceserver.ResourceServerExpressionInterceptUrlRegistryPostProcessor;
import com.c4_soft.springaddons.security.oidc.starter.synchronised.resourceserver.ResourceServerSynchronizedHttpSecurityPostProcessor;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;
import java.util.stream.Collectors;

import static dev.albertv.projects.api.core.config.security.Permission.CREATE_PROJECT;
import static dev.albertv.projects.api.core.config.security.Permission.CREATE_SKILL;
import static dev.albertv.projects.api.core.config.security.Permission.DELETE_PROJECT;
import static dev.albertv.projects.api.core.config.security.Permission.DELETE_SKILL;
import static dev.albertv.projects.api.core.config.security.Permission.READ_PROJECT;
import static dev.albertv.projects.api.core.config.security.Permission.READ_SKILL;
import static dev.albertv.projects.api.core.config.security.Permission.REVALIDATE_PORTFOLIO;
import static dev.albertv.projects.api.core.config.security.Permission.UPDATE_PROJECT;
import static dev.albertv.projects.api.core.config.security.Permission.UPDATE_SKILL;
import static dev.albertv.projects.api.core.config.security.Role.ANONYMOUS;
import static org.springframework.http.HttpMethod.DELETE;
import static org.springframework.http.HttpMethod.GET;
import static org.springframework.http.HttpMethod.PATCH;
import static org.springframework.http.HttpMethod.POST;
import static org.springframework.http.HttpMethod.PUT;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    @Bean
    ResourceServerExpressionInterceptUrlRegistryPostProcessor
        resourceServerExpressionInterceptUrlRegistryPostProcessor() {
        return auth -> auth
            .requestMatchers(
                "/v2/api-docs",
                "/v3/api-docs",
                "/v3/api-docs/**",
                "/swagger-resources",
                "/swagger-resources/**",
                "/configuration/ui",
                "/configuration/security",
                "/swagger-ui/**",
                "/webjars/**",
                "/swagger-ui.html"
            ).permitAll()

            .requestMatchers("/actuator/health/liveness", "/actuator/health/readiness").permitAll()

            .requestMatchers("/api/info", "/api/me").permitAll()

            .requestMatchers(GET, "/api/project/**").hasAuthority(READ_PROJECT.getName())
            .requestMatchers(POST, "/api/project/**").hasAuthority(CREATE_PROJECT.getName())
            .requestMatchers(PUT, "/api/project/**").hasAuthority(UPDATE_PROJECT.getName())
            .requestMatchers(PATCH, "/api/project/**").hasAuthority(UPDATE_PROJECT.getName())
            .requestMatchers(DELETE, "/api/project/**").hasAuthority(DELETE_PROJECT.getName())

            .requestMatchers(GET, "/api/skill/**").hasAuthority(READ_SKILL.getName())
            .requestMatchers(POST, "/api/skill/**").hasAuthority(CREATE_SKILL.getName())
            .requestMatchers(PUT, "/api/skill/**").hasAuthority(UPDATE_SKILL.getName())
            .requestMatchers(PATCH, "/api/skill/**").hasAuthority(UPDATE_SKILL.getName())
            .requestMatchers(DELETE, "/api/skill/**").hasAuthority(DELETE_SKILL.getName())

            .requestMatchers(POST, "/api/portfolio/revalidate").hasAuthority(REVALIDATE_PORTFOLIO.getName());
    }

    @Bean
    ResourceServerSynchronizedHttpSecurityPostProcessor resourceServerSynchronizedHttpSecurityPostProcessor() {
        return http -> http
            .anonymous(anonymous -> anonymous
                .authorities(ANONYMOUS.getPermissions())
            );
    }

    @Bean
    ClaimSetAuthoritiesConverter claimSetAuthoritiesConverter(
        final OpenidProviderPropertiesResolver openidProviderPropertiesResolver
    ) {
        final ClaimSetAuthoritiesConverter roleClaimSetAuthoritiesConverter =
            new ConfigurableClaimSetAuthoritiesConverter(openidProviderPropertiesResolver);

        return source -> {
            final Collection<? extends GrantedAuthority> roleAuthorities =
                roleClaimSetAuthoritiesConverter.convert(source);
            if (roleAuthorities == null) {
                return null;
            }

            return roleAuthorities.stream()
                .map(GrantedAuthority::getAuthority)
                .map(Role::valueOf)
                .flatMap(r -> r.getPermissions().stream())
                .collect(Collectors.toSet());
        };
    }

}
