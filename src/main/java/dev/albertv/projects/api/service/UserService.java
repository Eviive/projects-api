package dev.albertv.projects.api.service;

import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import dev.albertv.projects.api.core.exception.ProjectsApiException;
import dev.albertv.projects.api.dto.CurrentUserDTO;
import dev.albertv.projects.api.dto.auth.AuthResponseDTO;
import dev.albertv.projects.api.entity.User;
import dev.albertv.projects.api.mapper.UserMapper;
import dev.albertv.projects.api.repository.UserRepository;
import dev.albertv.projects.api.util.TokenUtils;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.WebAuthenticationDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;

import static dev.albertv.projects.api.core.exception.ProjectsApiErrorsEnum.API400_REFRESH_TOKEN_NOT_FOUND;
import static dev.albertv.projects.api.core.exception.ProjectsApiErrorsEnum.API401_LOGIN_FAILED;
import static dev.albertv.projects.api.core.exception.ProjectsApiErrorsEnum.API401_TOKEN_ERROR;
import static dev.albertv.projects.api.core.exception.ProjectsApiErrorsEnum.API404_USERNAME_NOT_FOUND;
import static dev.albertv.projects.api.core.exception.ProjectsApiErrorsEnum.API500_INTERNAL_SERVER_ERROR;

@Service
@RequiredArgsConstructor
public class UserService implements UserDetailsService {

    private final UserRepository userRepository;

    private final UserMapper userMapper;

    private final TokenUtils tokenUtils;

    private final AuthenticationConfiguration authenticationConfiguration;

    @Transactional(readOnly = true)
    public CurrentUserDTO getCurrentUser(final SecurityContext securityContext) {
        final Authentication authentication = securityContext.getAuthentication();
        final User user;

        if (authentication instanceof AnonymousAuthenticationToken) {
            user = null;
        } else {
            user = userRepository.findByUsername(authentication.getName())
                .orElseThrow(() -> ProjectsApiException.format(
                    API404_USERNAME_NOT_FOUND,
                    authentication.getName()
                ));
        }

        return userMapper.toCurrentDTO(user, authentication.getAuthorities());
    }

    @Transactional(readOnly = true)
    @SuppressWarnings("checkstyle:IllegalCatch")
    public AuthResponseDTO login(
        final String username,
        final String password,
        final HttpServletRequest req,
        final HttpServletResponse res
    ) {
        try {
            final AbstractAuthenticationToken abstractAuthenticationToken =
                new UsernamePasswordAuthenticationToken(username, password);

            abstractAuthenticationToken.setDetails(new WebAuthenticationDetails(req));

            final Authentication authentication = authenticationConfiguration
                .getAuthenticationManager()
                .authenticate(abstractAuthenticationToken);

            final User user = (User) authentication.getPrincipal();

            final Collection<? extends GrantedAuthority> authorities = user.getAuthorities();

            final String accessToken = tokenUtils.generateAccessToken(
                user.getUsername(),
                authorities
            );

            final CurrentUserDTO currentUser = userMapper.toCurrentDTO(user, authorities);

            res.addCookie(tokenUtils.generateRefreshTokenCookie(user.getUsername()));

            return new AuthResponseDTO(currentUser, accessToken);
        } catch (AuthenticationException e) {
            throw ProjectsApiException.format(e, API401_LOGIN_FAILED, e.getMessage());
        } catch (Exception e) {
            throw ProjectsApiException.format(e, API500_INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }

    public void logout(final HttpServletResponse res) {
        res.addCookie(tokenUtils.deleteCookie());
    }

    @Transactional(readOnly = true)
    public AuthResponseDTO refreshToken(final String refreshToken) {
        if (refreshToken == null) {
            throw new ProjectsApiException(API400_REFRESH_TOKEN_NOT_FOUND);
        }

        try {
            final DecodedJWT decodedToken = tokenUtils.verifyToken(refreshToken);

            final User user = userRepository.findByUsername(decodedToken.getSubject())
                .orElseThrow(() -> ProjectsApiException.format(
                    API404_USERNAME_NOT_FOUND,
                    decodedToken.getSubject()
                ));

            final Collection<? extends GrantedAuthority> authorities = user.getAuthorities();

            final String accessToken = tokenUtils.generateAccessToken(
                user.getUsername(),
                authorities
            );

            final CurrentUserDTO currentUser = userMapper.toCurrentDTO(user, authorities);

            return new AuthResponseDTO(currentUser, accessToken);
        } catch (JWTVerificationException e) {
            throw ProjectsApiException.format(e, API401_TOKEN_ERROR, e.getMessage());
        }
    }

    @Override
    @Transactional(readOnly = true)
    public UserDetails loadUserByUsername(final String username) throws UsernameNotFoundException {
        return userRepository.findByUsername(username)
            .orElseThrow(() ->
                new UsernameNotFoundException("User %s not found".formatted(username))
            );
    }

}
