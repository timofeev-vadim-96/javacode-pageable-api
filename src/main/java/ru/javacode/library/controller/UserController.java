package ru.javacode.library.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import ru.javacode.library.controller.dto.ChangeLockStateRequest;
import ru.javacode.library.service.UserService;

@RestController
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @PatchMapping("/api/v1/user/block")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<Void> changeLockState(@Valid @RequestBody ChangeLockStateRequest request) {
        userService.setIsBlock(request.getEmail(), request.getIsBlocked());
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
