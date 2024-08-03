package com.eviive.personalapi.config;

import com.eviive.personalapi.config.exception.PersonalApiExceptionHandler;
import com.eviive.personalapi.entity.Role;
import com.eviive.personalapi.filter.AuthorizationFilter;
import com.eviive.personalapi.properties.CorsPropertiesConfig;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.access.hierarchicalroles.RoleHierarchy;
import org.springframework.security.access.hierarchicalroles.RoleHierarchyImpl;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.expression.DefaultWebSecurityExpressionHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;
import java.util.List;

import static com.eviive.personalapi.entity.Role.ANONYMOUS;
import static com.eviive.personalapi.entity.Scope.*;
import static org.springframework.http.HttpHeaders.AUTHORIZATION;
import static org.springframework.http.HttpHeaders.CONTENT_TYPE;
import static org.springframework.http.HttpHeaders.ORIGIN;
import static org.springframework.http.HttpMethod.DELETE;
import static org.springframework.http.HttpMethod.GET;
import static org.springframework.http.HttpMethod.PATCH;
import static org.springframework.http.HttpMethod.POST;
import static org.springframework.http.HttpMethod.PUT;
import static org.springframework.security.config.http.SessionCreationPolicy.STATELESS;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final AuthorizationFilter authorizationFilter;

    private final PersonalApiExceptionHandler personalApiExceptionHandler;

    @Bean
    @SuppressWarnings({"checkstyle:LambdaBodyLength", "checkstyle:MultipleStringLiterals"})
    public SecurityFilterChain filterChain(final HttpSecurity http) throws Exception {
        return http
            .csrf(AbstractHttpConfigurer::disable)
            .cors(Customizer.withDefaults())

            .sessionManagement(session -> session.sessionCreationPolicy(STATELESS))

            .addFilterBefore(authorizationFilter, UsernamePasswordAuthenticationFilter.class)
            .anonymous(anonymous -> anonymous.authorities(ANONYMOUS.getAuthorities()))

            .authorizeHttpRequests(auth ->
                auth.requestMatchers(
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
                    )
                    .permitAll()

                    .requestMatchers(GET, "/user/current")
                    .permitAll()
                    .requestMatchers(POST, "/user/login", "/user/logout", "/user/refresh")
                    .permitAll()

                    .requestMatchers(GET, "/project/**")
                    .hasAuthority(READ_PROJECT.getName())
                    .requestMatchers(POST, "/project/**")
                    .hasAuthority(CREATE_PROJECT.getName())
                    .requestMatchers(PUT, "/project/**")
                    .hasAuthority(UPDATE_PROJECT.getName())
                    .requestMatchers(PATCH, "/project/**")
                    .hasAuthority(UPDATE_PROJECT.getName())
                    .requestMatchers(DELETE, "/project/**")
                    .hasAuthority(DELETE_PROJECT.getName())

                    .requestMatchers(GET, "/skill/**")
                    .hasAuthority(READ_SKILL.getName())
                    .requestMatchers(POST, "/skill/**")
                    .hasAuthority(CREATE_SKILL.getName())
                    .requestMatchers(PUT, "/skill/**")
                    .hasAuthority(UPDATE_SKILL.getName())
                    .requestMatchers(PATCH, "/skill/**")
                    .hasAuthority(UPDATE_SKILL.getName())
                    .requestMatchers(DELETE, "/skill/**")
                    .hasAuthority(DELETE_SKILL.getName())

                    .requestMatchers(POST, "/portfolio/revalidate")
                    .hasAuthority(REVALIDATE_PORTFOLIO.getName())

                    .requestMatchers(GET, "/actuator/info", "/actuator/health")
                    .permitAll()
                    .requestMatchers(GET, "/actuator/**")
                    .hasAuthority(READ_ACTUATOR.getName())

                    // deny-by-default policy
                    .anyRequest()
                    .denyAll()
            )

            .exceptionHandling(exceptionHandling ->
                exceptionHandling
                    .authenticationEntryPoint(personalApiExceptionHandler)
                    .accessDeniedHandler(personalApiExceptionHandler)
            )
            .build();
    }

    @Bean
    public RoleHierarchy roleHierarchy() {
        final RoleHierarchyImpl roleHierarchy = new RoleHierarchyImpl();
        final Role[] roles = Role.values();
        final StringBuilder roleHierarchyBuilder = new StringBuilder();

        Arrays
            .stream(roles)
            .filter(r -> r.getSubRoles() != null)
            .forEach(r -> {
                final String roleName = r.toString();

                for (Role subRole: r.getSubRoles()) {
                    roleHierarchyBuilder
                        .append(roleName)
                        .append(" > ")
                        .append(subRole.toString())
                        .append("\n");
                }
            });

        roleHierarchy.setHierarchy(roleHierarchyBuilder.toString());
        return roleHierarchy;
    }

    @Bean
    public DefaultWebSecurityExpressionHandler customWebSecurityExpressionHandler(final RoleHierarchy roleHierarchy) {
        final DefaultWebSecurityExpressionHandler expressionHandler = new DefaultWebSecurityExpressionHandler();
        expressionHandler.setRoleHierarchy(roleHierarchy);
        return expressionHandler;
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource(final CorsPropertiesConfig corsPropertiesConfig) {
        final CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(corsPropertiesConfig.allowedOrigins());
        configuration.addAllowedMethod("*");
        configuration.setAllowedHeaders(List.of(AUTHORIZATION, ORIGIN, CONTENT_TYPE));
        configuration.setAllowCredentials(true);
        final UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

}
