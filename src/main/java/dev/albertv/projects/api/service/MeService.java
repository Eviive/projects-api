package dev.albertv.projects.api.service;

import dev.albertv.projects.api.dto.CurrentUserDTO;
import dev.albertv.projects.api.util.HashUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.core.oidc.StandardClaimNames;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MeService {

    private final HashUtils hashUtils;

    public CurrentUserDTO findMe() {
        final Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        final Set<String> authorities = authentication.getAuthorities()
            .stream()
            .map(GrantedAuthority::getAuthority)
            .collect(Collectors.toSet());

        if (authentication instanceof JwtAuthenticationToken jwtAuthenticationToken) {
            final Map<String, Object> tokenAttributes = jwtAuthenticationToken.getTokenAttributes();

            final String email = (String) tokenAttributes.get(StandardClaimNames.EMAIL);

            final String name = (String) tokenAttributes.get(StandardClaimNames.NAME);

            final String avatar = "https://gravatar.com/avatar/%s?default=retro"
                .formatted(hashUtils.sha256Hex(email));

            return new CurrentUserDTO(
                authentication.getName(),
                email,
                name,
                avatar,
                authorities
            );
        }

        return new CurrentUserDTO(
            null,
            null,
            "Guest",
            null,
            authorities
        );
    }

}
