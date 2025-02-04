package ru.javacode.library.security;

import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import ru.javacode.library.controller.dto.JwtAuthenticationResponse;
import ru.javacode.library.controller.dto.SignInRequest;
import ru.javacode.library.controller.dto.SignUpRequest;
import ru.javacode.library.exception.EntityNotFoundException;
import ru.javacode.library.exception.TokenNotValidException;
import ru.javacode.library.model.User;
import ru.javacode.library.service.UserService;

import ru.javacode.library.event.EventAuthenticationSuccessEvent;

@Service
@RequiredArgsConstructor
public class AuthService {
    public static final String BEARER_PREFIX = "Bearer ";

    public static final String HEADER_NAME = "Authorization";

    private final UserService userService;

    private final JwtService jwtService;

    private final PasswordEncoder passwordEncoder;

    private final AuthenticationManager authenticationManager;

    private final ApplicationEventPublisher eventPublisher;

    /**
     * Регистрация пользователя
     *
     * @param request данные пользователя
     * @return токен
     */
    public JwtAuthenticationResponse signUp(SignUpRequest request) {

        var user = User.builder()
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .role(request.getRole())
                .build();

        User created = userService.create(user);

        var accessToken = jwtService.generateToken(created);
        var refreshToken = jwtService.generateRefreshToken(created);

        return new JwtAuthenticationResponse(accessToken, refreshToken);
    }

    /**
     * Аутентификация пользователя
     *
     * @param request данные пользователя
     * @return токен
     */
    public JwtAuthenticationResponse signIn(SignInRequest request) {
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                request.getEmail(),
                request.getPassword()
        ));

        eventPublisher.publishEvent(new EventAuthenticationSuccessEvent(this, request.getEmail()));

        var user = userService.getUserByEmail(request.getEmail());

        var accessToken = jwtService.generateToken(user);
        var refreshToken = jwtService.generateRefreshToken(user);

        return new JwtAuthenticationResponse(accessToken, refreshToken);
    }

    public JwtAuthenticationResponse refreshToken(String refreshToken) {
        // Получаем токен из заголовка
        if (StringUtils.startsWith(refreshToken, BEARER_PREFIX)) {
            // Обрезаем префикс и получаем имя пользователя из токена
            var jwt = refreshToken.substring(BEARER_PREFIX.length());
            var username = jwtService.extractUserName(jwt);

            if (StringUtils.isNotEmpty(username)) {
                var user = userService.getUserByEmail(username);

                if (jwtService.isTokenValid(jwt, user)) {
                    var accessToken = jwtService.generateToken(user);
                    var newRefreshToken = jwtService.generateRefreshToken(user);

                    return new JwtAuthenticationResponse(accessToken, newRefreshToken);
                }
            }
        }
        throw new TokenNotValidException("Token is not valid");
    }
}
