package ru.javacode.library.controller.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class JwtRefreshRequest {
    @NotBlank(message = "Jwt must not be empty or null")
    private String refreshToken;
}
