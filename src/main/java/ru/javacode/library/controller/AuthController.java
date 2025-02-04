package ru.javacode.library.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import ru.javacode.library.controller.dto.JwtAuthenticationResponse;
import ru.javacode.library.controller.dto.JwtRefreshRequest;
import ru.javacode.library.controller.dto.SignInRequest;
import ru.javacode.library.controller.dto.SignUpRequest;
import ru.javacode.library.security.AuthService;
import ru.javacode.library.util.Role;

@RestController
@RequiredArgsConstructor
public class AuthController {
    private final AuthService authenticationService;

    /**
     * Если мы хотим зарегистрировать админа, то мы уже должны быть авторизованы в качестве админа
     */
    @PostMapping("/api/v1/sign-up")
    public ResponseEntity<JwtAuthenticationResponse> signUp(@AuthenticationPrincipal UserDetails userDetails,
                                                            @RequestBody @Valid SignUpRequest request) {
        if (request.getRole().equals(Role.ROLE_ADMIN) && (userDetails == null ||
                !userDetails.getAuthorities().contains(new SimpleGrantedAuthority(Role.ROLE_ADMIN.name())))) {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }
        var tokens = authenticationService.signUp(request);
        return new ResponseEntity<>(tokens, HttpStatus.OK);
    }

    @PostMapping("/api/v1/sign-in")
    public ResponseEntity<JwtAuthenticationResponse> signIn(@RequestBody @Valid SignInRequest request) {
        var tokens = authenticationService.signIn(request);
        return new ResponseEntity<>(tokens, HttpStatus.OK);
    }

    @PostMapping("/api/v1/token/refresh")
    public ResponseEntity<JwtAuthenticationResponse> refreshToken(@RequestBody @Valid JwtRefreshRequest refreshToken) {
        var tokens = authenticationService.refreshToken(refreshToken.getRefreshToken());
        return new ResponseEntity<>(tokens, HttpStatus.OK);
    }
}
