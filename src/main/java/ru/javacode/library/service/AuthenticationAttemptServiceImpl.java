package ru.javacode.library.service;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import ru.javacode.library.repository.AuthenticationAttemptRepository;
import ru.javacode.library.security.config.SecurityProperties;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AuthenticationAttemptServiceImpl implements AuthenticationAttemptService {
    private final AuthenticationAttemptRepository authenticationAttemptRepository;

    private final UserService userService;

    private final SecurityProperties securityProperties;

    private static final Logger logger = LoggerFactory.getLogger(AuthenticationAttemptServiceImpl.class);

    /**
     * Обнулить счетчик попыток аутентификации.
     * @param email email пользователя
     */
    @Override
    public void clearAttempts(String email) {
        authenticationAttemptRepository.clearAttemptsByEmail(email);
    }

    /**
     * Увеличить счетчик попыток аутентификации. Если счетчик превысил допустимое значение, то заблокировать аккаунт
     * @param email email пользователя
     */
    @Override
    public void increaseAttempts(String email) {
        authenticationAttemptRepository.increaseAttemptsByEmail(email);

        Optional<Integer> attempts = authenticationAttemptRepository.getAttemptsByEmail(email);

        if (attempts.isPresent()) {
            int attemptsRemaining = securityProperties.getMaxLoginAttempts() - attempts.get();
            logger.info("Оставшихся попыток доступа к аккаунта по email {} : {}", email, attemptsRemaining);
            if (attempts.get() >= securityProperties.getMaxLoginAttempts()) {
                userService.setIsBlock(email, true);
            }
        }
    }
}
