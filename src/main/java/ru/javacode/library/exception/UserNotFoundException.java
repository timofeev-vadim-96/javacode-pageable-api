package ru.javacode.library.exception;

import org.springframework.dao.DataAccessException;

public class UserNotFoundException extends DataAccessException {
    public UserNotFoundException(String message) {
        super(message);
    }
}
