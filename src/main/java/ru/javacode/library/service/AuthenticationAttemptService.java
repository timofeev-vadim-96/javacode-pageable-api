package ru.javacode.library.service;

public interface AuthenticationAttemptService {
    void clearAttempts(String email);

    void increaseAttempts(String email);
}
