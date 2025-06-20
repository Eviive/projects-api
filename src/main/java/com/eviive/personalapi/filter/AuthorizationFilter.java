package com.eviive.personalapi.filter;

import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.Claim;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.eviive.personalapi.util.TokenUtilities;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;

import static com.eviive.personalapi.util.TokenUtilities.AUTHORITIES_CLAIM;
import static org.springframework.http.HttpHeaders.AUTHORIZATION;

@Component
@RequiredArgsConstructor
public class AuthorizationFilter extends OncePerRequestFilter {

    private static final String BEARER_PREFIX = "Bearer ";

    private final TokenUtilities tokenUtilities;

    @Override
    protected void doFilterInternal(
        @NotNull final HttpServletRequest req,
        @NotNull final HttpServletResponse res,
        @NotNull final FilterChain filterChain
    )
        throws ServletException, IOException {
        final String authorizationHeader = req.getHeader(AUTHORIZATION);

        if (authorizationHeader == null) {
            filterChain.doFilter(req, res);
            return;
        }

        try {
            if (!authorizationHeader.startsWith(BEARER_PREFIX)) {
                throw new IllegalStateException("The access token must be a bearer token.");
            }

            final String token = authorizationHeader.substring(BEARER_PREFIX.length());

            final DecodedJWT decodedToken = tokenUtilities.verifyToken(token);

            final String username = decodedToken.getSubject();

            final Claim claim = decodedToken.getClaim(AUTHORITIES_CLAIM);

            if (claim.isMissing() || claim.isNull()) {
                throw new IllegalStateException("Authorities are missing from the token.");
            }

            final List<SimpleGrantedAuthority> authorities = claim
                .asList(String.class)
                .stream()
                .map(SimpleGrantedAuthority::new)
                .toList();

            final AbstractAuthenticationToken abstractAuthenticationToken =
                new UsernamePasswordAuthenticationToken(
                    username,
                    null,
                    authorities
                );

            abstractAuthenticationToken.setDetails(new WebAuthenticationDetails(req));

            SecurityContextHolder
                .getContext()
                .setAuthentication(abstractAuthenticationToken);
        } catch (IllegalStateException | JWTVerificationException e) {
            if (logger.isDebugEnabled()) {
                logger.debug("Authentication failed: %s".formatted(e.getMessage()), e);
            }
        } finally {
            filterChain.doFilter(req, res);
        }
    }

}
