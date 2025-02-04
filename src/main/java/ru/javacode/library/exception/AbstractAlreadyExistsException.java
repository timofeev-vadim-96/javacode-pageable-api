package ru.javacode.library.exception;

import org.springframework.dao.DataAccessException;

public abstract class AbstractAlreadyExistsException extends DataAccessException {
    public AbstractAlreadyExistsException(String msg) {
        super(msg);
    }
}
