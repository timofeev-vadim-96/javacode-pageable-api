package ru.javacode.library.controller.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
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
    @JsonProperty("is_blocked")
    private Boolean isBlocked;
}
