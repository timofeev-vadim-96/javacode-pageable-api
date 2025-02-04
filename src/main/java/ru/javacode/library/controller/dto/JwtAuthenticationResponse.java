package ru.javacode.library.controller.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class JwtAuthenticationResponse {
    private String token;
}
