package ru.javacode.library.controller.dto;

import jakarta.validation.constraints.Email;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NonNull;

@AllArgsConstructor
@Getter
public class ChangeLockStateRequest {
    @Email
    private String email;

    @NonNull
    private Boolean isBlocked;
}
