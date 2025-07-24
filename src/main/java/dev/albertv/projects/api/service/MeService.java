package dev.albertv.projects.api.service;

import dev.albertv.projects.api.dto.CurrentUserDTO;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.core.oidc.StandardClaimNames;
import org.springframework.security.oauth2.jwt.JwtClaimNames;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Date;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class MeService {

    public CurrentUserDTO findMe(final Authentication authentication) {
        final Set<String> authorities = authentication.getAuthorities()
            .stream()
            .map(GrantedAuthority::getAuthority)
            .collect(Collectors.toSet());

        if (authentication instanceof JwtAuthenticationToken jwtAuthenticationToken) {
            final Map<String, Object> tokenAttributes = jwtAuthenticationToken.getTokenAttributes();

            final String email = (String) tokenAttributes.get(StandardClaimNames.EMAIL);

            final Long exp = switch (tokenAttributes.get(JwtClaimNames.EXP)) {
                case Long lexp -> lexp;
                case Instant iexp -> iexp.getEpochSecond();
                case Date dexp -> dexp.toInstant().getEpochSecond();
                default -> null;
            };

            return new CurrentUserDTO(authentication.getName(), email, authorities, exp);
        }

        return new CurrentUserDTO(
            "Guest",
            null,
            authorities,
            null
        );
    }

}
