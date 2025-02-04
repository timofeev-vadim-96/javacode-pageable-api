package ru.javacode.library.repository;

import java.util.Optional;

public interface AuthenticationAttemptRepository {
    Optional<Integer> getAttemptsByEmail(String email);

    void increaseAttemptsByEmail(String email);

    void clearAttemptsByEmail(String email);
}
