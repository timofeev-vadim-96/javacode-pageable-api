package ru.javacode.library.repository;

import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class InMemoryAuthenticationAttemptRepository implements AuthenticationAttemptRepository{
    private final Map<String, Integer> failLoginAttempts = new ConcurrentHashMap<>();

    @Override
    public Optional<Integer> getAttemptsByEmail(String email) {
        return Optional.ofNullable(failLoginAttempts.get(email));
    }

    @Override
    public void increaseAttemptsByEmail(String email) {
        failLoginAttempts.put(email, failLoginAttempts.getOrDefault(email, 0) + 1);
    }

    @Override
    public void clearAttemptsByEmail(String email) {
        failLoginAttempts.remove(email);
    }
}
