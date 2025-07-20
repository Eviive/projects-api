package dev.albertv.projects.api.core.config;

import dev.albertv.projects.api.core.exception.ProjectsApiExceptionHandler;
import dev.albertv.projects.api.core.properties.CorsPropertiesConfig;
import dev.albertv.projects.api.entity.Role;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.access.hierarchicalroles.RoleHierarchy;
import org.springframework.security.access.hierarchicalroles.RoleHierarchyImpl;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final ProjectsApiExceptionHandler projectsApiExceptionHandler;

    private final CorsPropertiesConfig corsPropertiesConfig;

//    @Bean
//    SecurityFilterChain filterChain(final HttpSecurity http) throws Exception {
//        return http
//            .csrf(AbstractHttpConfigurer::disable)
//            .cors(withDefaults())
//
//            .sessionManagement(session ->
//                session.sessionCreationPolicy(STATELESS)
//            )
//
//            .addFilterBefore(authorizationFilter, UsernamePasswordAuthenticationFilter.class)
//            .anonymous(anonymous ->
//                anonymous.authorities(ANONYMOUS.getAuthorities())
//            )
//
//            .authorizeHttpRequests(auth ->
//                auth.requestMatchers(
//                        "/v2/api-docs",
//                        "/v3/api-docs",
//                        "/v3/api-docs/**",
//                        "/swagger-resources",
//                        "/swagger-resources/**",
//                        "/configuration/ui",
//                        "/configuration/security",
//                        "/swagger-ui/**",
//                        "/webjars/**",
//                        "/swagger-ui.html"
//                    )
//                    .permitAll()
//
//                    .requestMatchers(GET, "/user/current").permitAll()
//                    .requestMatchers(POST, "/user/login", "/user/logout", "/user/refresh").permitAll()
//
//                    .requestMatchers(GET, "/project/**").hasAuthority(READ_PROJECT.getName())
//                    .requestMatchers(POST, "/project/**").hasAuthority(CREATE_PROJECT.getName())
//                    .requestMatchers(PUT, "/project/**").hasAuthority(UPDATE_PROJECT.getName())
//                    .requestMatchers(PATCH, "/project/**").hasAuthority(UPDATE_PROJECT.getName())
//                    .requestMatchers(DELETE, "/project/**").hasAuthority(DELETE_PROJECT.getName())
//
//                    .requestMatchers(GET, "/skill/**").hasAuthority(READ_SKILL.getName())
//                    .requestMatchers(POST, "/skill/**").hasAuthority(CREATE_SKILL.getName())
//                    .requestMatchers(PUT, "/skill/**").hasAuthority(UPDATE_SKILL.getName())
//                    .requestMatchers(PATCH, "/skill/**").hasAuthority(UPDATE_SKILL.getName())
//                    .requestMatchers(DELETE, "/skill/**").hasAuthority(DELETE_SKILL.getName())
//
//                    .requestMatchers(POST, "/portfolio/revalidate").hasAuthority(REVALIDATE_PORTFOLIO.getName())
//
//                    .requestMatchers(GET, "/actuator/info", "/actuator/health").permitAll()
//                    .requestMatchers(GET, "/actuator/**").hasAuthority(READ_ACTUATOR.getName())
//
//                    // deny-by-default policy
//                    .anyRequest().denyAll()
//            )
//
//            .exceptionHandling(exceptionHandling ->
//                exceptionHandling
//                    .authenticationEntryPoint(projectsApiExceptionHandler)
//                    .accessDeniedHandler(projectsApiExceptionHandler)
//            )
//            .build();
//    }

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

    @Bean
    CorsConfigurationSource corsConfigurationSource() {
        final CorsConfiguration configuration = corsPropertiesConfig.getConfiguration();
        final UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }

}
