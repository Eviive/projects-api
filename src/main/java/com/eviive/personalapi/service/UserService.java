package com.eviive.personalapi.service;

import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.eviive.personalapi.config.exception.PersonalApiException;
import com.eviive.personalapi.dto.CurrentUserDTO;
import com.eviive.personalapi.dto.auth.AuthResponseDTO;
import com.eviive.personalapi.entity.User;
import com.eviive.personalapi.mapper.UserMapper;
import com.eviive.personalapi.repository.UserRepository;
import com.eviive.personalapi.util.TokenUtilities;
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

import static com.eviive.personalapi.config.exception.PersonalApiErrorsEnum.API400_REFRESH_TOKEN_NOT_FOUND;
import static com.eviive.personalapi.config.exception.PersonalApiErrorsEnum.API401_LOGIN_FAILED;
import static com.eviive.personalapi.config.exception.PersonalApiErrorsEnum.API401_TOKEN_ERROR;
import static com.eviive.personalapi.config.exception.PersonalApiErrorsEnum.API404_USERNAME_NOT_FOUND;
import static com.eviive.personalapi.config.exception.PersonalApiErrorsEnum.API500_INTERNAL_SERVER_ERROR;

@Service
@RequiredArgsConstructor
public class UserService implements UserDetailsService {

    private final UserRepository userRepository;

    private final TokenUtilities tokenUtilities;

    private final AuthenticationConfiguration authenticationConfiguration;

    private final UserMapper userMapper;

    @Transactional(readOnly = true)
    public CurrentUserDTO getCurrentUser(final SecurityContext securityContext) {
        final Authentication authentication = securityContext.getAuthentication();
        final User user;

        if (authentication instanceof AnonymousAuthenticationToken) {
            user = null;
        } else {
            user = userRepository.findByUsername(authentication.getName())
                .orElseThrow(() -> PersonalApiException.format(
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

            final String accessToken = tokenUtilities.generateAccessToken(
                user.getUsername(),
                authorities
            );

            final CurrentUserDTO currentUser = userMapper.toCurrentDTO(user, authorities);

            res.addCookie(tokenUtilities.generateRefreshTokenCookie(user.getUsername()));

            return new AuthResponseDTO(currentUser, accessToken);
        } catch (AuthenticationException e) {
            throw PersonalApiException.format(e, API401_LOGIN_FAILED, e.getMessage());
        } catch (Exception e) {
            throw PersonalApiException.format(e, API500_INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }

    public void logout(final HttpServletResponse res) {
        res.addCookie(tokenUtilities.deleteCookie());
    }

    @Transactional(readOnly = true)
    public AuthResponseDTO refreshToken(final String refreshToken) {
        if (refreshToken == null) {
            throw new PersonalApiException(API400_REFRESH_TOKEN_NOT_FOUND);
        }

        try {
            final DecodedJWT decodedToken = tokenUtilities.verifyToken(refreshToken);

            final User user = userRepository.findByUsername(decodedToken.getSubject())
                .orElseThrow(() -> PersonalApiException.format(
                    API404_USERNAME_NOT_FOUND,
                    decodedToken.getSubject()
                ));

            final Collection<? extends GrantedAuthority> authorities = user.getAuthorities();

            final String accessToken = tokenUtilities.generateAccessToken(
                user.getUsername(),
                authorities
            );

            final CurrentUserDTO currentUser = userMapper.toCurrentDTO(user, authorities);

            return new AuthResponseDTO(currentUser, accessToken);
        } catch (JWTVerificationException e) {
            throw PersonalApiException.format(e, API401_TOKEN_ERROR, e.getMessage());
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
