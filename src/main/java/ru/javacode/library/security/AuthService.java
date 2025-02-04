package ru.javacode.library.security;

import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import ru.javacode.library.controller.dto.JwtAuthenticationResponse;
import ru.javacode.library.controller.dto.SignInRequest;
import ru.javacode.library.controller.dto.SignUpRequest;
import ru.javacode.library.model.User;
import ru.javacode.library.service.UserService;

import ru.javacode.library.event.EventAuthenticationSuccessEvent;

@Service
@RequiredArgsConstructor
public class AuthService {
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

        var jwt = jwtService.generateToken(created);
        return new JwtAuthenticationResponse(jwt);
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

        var jwt = jwtService.generateToken(user);
        return new JwtAuthenticationResponse(jwt);
    }
}
