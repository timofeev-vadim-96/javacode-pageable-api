package ru.javacode.library.exception;

public class TitleAlreadyExistsException extends AbstractAlreadyExistsException {
    public TitleAlreadyExistsException(String message) {
        super(message);
    }
}
