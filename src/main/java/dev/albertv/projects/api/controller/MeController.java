package dev.albertv.projects.api.controller;

import dev.albertv.projects.api.dto.CurrentUserDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.core.oidc.StandardClaimNames;
import org.springframework.security.oauth2.jwt.JwtClaimNames;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.Instant;
import java.util.Date;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RestController
@RequestMapping("/api/me")
@RequiredArgsConstructor
@Tag(name = "Me")
public class MeController {

    // GET

    @GetMapping(produces = APPLICATION_JSON_VALUE)
    @Operation(
        summary = "Me",
        responses = @ApiResponse(responseCode = "200", description = "OK")
    )
    public CurrentUserDTO findMe(Authentication authentication) {
        if (authentication instanceof JwtAuthenticationToken jwtAuthenticationToken) {
            Map<String, Object> tokenAttributes = jwtAuthenticationToken.getTokenAttributes();

            final String email = (String) tokenAttributes.get(StandardClaimNames.EMAIL);

            final Set<String> roles = jwtAuthenticationToken.getAuthorities()
                .stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toSet());

            final Long exp = switch (tokenAttributes.get(JwtClaimNames.EXP)) {
                case Long lexp -> lexp;
                case Instant iexp -> iexp.getEpochSecond();
                case Date dexp -> dexp.toInstant().getEpochSecond();
                default -> null;
            };

            return new CurrentUserDTO(authentication.getName(), email, roles, exp);
        }

        return CurrentUserDTO.anonymous();
    }

}
