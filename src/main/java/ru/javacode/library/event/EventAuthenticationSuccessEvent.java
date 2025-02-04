package ru.javacode.library.event;

import lombok.Getter;
import org.springframework.context.ApplicationEvent;

@Getter
public class EventAuthenticationSuccessEvent extends ApplicationEvent {
    private final String email;

    public EventAuthenticationSuccessEvent(Object source, String email) {
        super(source);
        this.email = email;
    }
}

