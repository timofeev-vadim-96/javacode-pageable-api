package ru.javacode.library.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.security.sasl.AuthenticationException;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {
    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<Void> handleEntityNotFoundException(EntityNotFoundException e) {
        log.warn(e.getMessage());
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(IdNullPointerException.class)
    public ResponseEntity<Void> handleIdNullPointerException(IdNullPointerException e) {
        log.warn(e.getMessage());
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(TokenNotValidException.class)
    public ResponseEntity<Void> handleTokenNotValidException(TokenNotValidException e) {
        log.warn(e.getMessage());
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }

    /**
     * Catches exceptions when the uniqueness of fields in the database is violated
     */
    @ExceptionHandler(AbstractAlreadyExistsException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public ResponseEntity<Void> handleAbstractAlreadyExistsException(AbstractAlreadyExistsException e) {
        log.warn(e.getMessage());
        return new ResponseEntity<>(HttpStatus.CONFLICT);
    }

    /**
     * Catches authentication errors
     */
    @ExceptionHandler(AuthenticationException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public ResponseEntity<Void> handleAuthenticationException(AuthenticationException e) {
        log.warn(e.getMessage());
        return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
    }

    /**
     * Catch exceptions when trying to authenticate with a non-existent email
     */
    @ExceptionHandler(UserNotFoundException.class)
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public ResponseEntity<Void> handleUserNotFoundException(UserNotFoundException e) {
        log.warn(e.getMessage());
        return new ResponseEntity<>(HttpStatus.FORBIDDEN);
    }
}
