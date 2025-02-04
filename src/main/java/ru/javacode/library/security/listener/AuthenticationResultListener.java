package ru.javacode.library.security.listener;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.event.EventListener;
import org.springframework.security.authentication.event.AuthenticationFailureBadCredentialsEvent;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;
import ru.javacode.library.event.EventAuthenticationSuccessEvent;
import ru.javacode.library.service.AuthenticationAttemptService;

@Component
@RequiredArgsConstructor
public class AuthenticationResultListener {
    private final AuthenticationAttemptService authenticationAttemptService;

    private static final Logger logger = LoggerFactory.getLogger(AuthenticationResultListener.class);

    /**
     * Если попытка аутентификации неудачна - увеличить счетчик попыток
     */
    @EventListener(AuthenticationFailureBadCredentialsEvent.class)
    public void onApplicationEvent(AuthenticationFailureBadCredentialsEvent event) {
        String email = extractUsername(event.getAuthentication());
        if (email != null) {
            authenticationAttemptService.increaseAttempts(email);
            logger.warn("Неудачная попытка аутентификации по email = {}", email);
        }
    }

    /**
     * Если пользователь аутентифицировался в рамках допускаемого количества попыток, то сбросить счетчик
     */
    @EventListener(EventAuthenticationSuccessEvent.class)
    public void onApplicationEvent(EventAuthenticationSuccessEvent event) {
        String email = event.getEmail();
        if (email != null) {
            logger.info("Успешная попытка аутентификации по email = {}", email);
            authenticationAttemptService.clearAttempts(email);
        }
    }

    private String extractUsername(Authentication authentication) {
        Object principal = authentication.getPrincipal();
        return principal instanceof String ? (String) principal : null;
    }
}
