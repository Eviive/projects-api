package dev.albertv.projects.api.core.config;

import com.c4_soft.springaddons.security.oidc.starter.synchronised.resourceserver.ResourceServerExpressionInterceptUrlRegistryPostProcessor;
import com.c4_soft.springaddons.security.oidc.starter.synchronised.resourceserver.ResourceServerSynchronizedHttpSecurityPostProcessor;
import dev.albertv.projects.api.entity.Role;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.access.hierarchicalroles.RoleHierarchy;
import org.springframework.security.access.hierarchicalroles.RoleHierarchyImpl;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;

import static dev.albertv.projects.api.entity.Role.ANONYMOUS;
import static dev.albertv.projects.api.entity.Scope.CREATE_PROJECT;
import static dev.albertv.projects.api.entity.Scope.CREATE_SKILL;
import static dev.albertv.projects.api.entity.Scope.DELETE_PROJECT;
import static dev.albertv.projects.api.entity.Scope.DELETE_SKILL;
import static dev.albertv.projects.api.entity.Scope.READ_PROJECT;
import static dev.albertv.projects.api.entity.Scope.READ_SKILL;
import static dev.albertv.projects.api.entity.Scope.REVALIDATE_PORTFOLIO;
import static dev.albertv.projects.api.entity.Scope.UPDATE_PROJECT;
import static dev.albertv.projects.api.entity.Scope.UPDATE_SKILL;
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
    ResourceServerSynchronizedHttpSecurityPostProcessor resourceServerSynchronizedHttpSecurityPostProcessor() {
        return http -> http
            .anonymous(anonymous -> anonymous
                .authorities(ANONYMOUS.getAuthorities())
            );
    }

    @Bean
    ResourceServerExpressionInterceptUrlRegistryPostProcessor resourceServerExpressionInterceptUrlRegistryPostProcessor() {
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
    RoleHierarchy roleHierarchy() {
        final Role[] roles = Role.values();

        final RoleHierarchyImpl.Builder builder = RoleHierarchyImpl.withDefaultRolePrefix();

        for (Role r : roles) {
            final RoleHierarchyImpl.Builder.ImpliedRoles roleBuilder =
                builder.role(r.name());

            if (r.getImplies() != null) {
                roleBuilder.implies(
                    r
                        .getImplies()
                        .stream()
                        .map(Role::name)
                        .toArray(String[]::new)
                );
            }
        }

        return builder.build();
    }

}
