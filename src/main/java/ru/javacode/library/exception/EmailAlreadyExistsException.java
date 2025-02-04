package ru.javacode.library.exception;

public class EmailAlreadyExistsException extends AbstractAlreadyExistsException {
    public EmailAlreadyExistsException(String message) {
        super(message);
    }
}
