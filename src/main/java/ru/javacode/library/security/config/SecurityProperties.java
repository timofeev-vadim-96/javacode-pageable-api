package ru.javacode.library.security.config;

import lombok.Getter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.bind.ConstructorBinding;

@ConfigurationProperties(prefix = "application.security")
@Getter
public class SecurityProperties {
    private final int maxLoginAttempts;

    @ConstructorBinding
    public SecurityProperties(int maxLoginAttempts) {
        this.maxLoginAttempts = maxLoginAttempts;
    }
}
