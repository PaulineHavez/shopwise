package com.shopwise.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler({
            CustomerNotFoundException.class,
            MerchantNotFoundException.class,
            ServiceNotFoundException.class,
            AppointmentNotFoundException.class
    })
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public Map<String, String> handleNotFound(RuntimeException ex) {
        return Map.of("error", ex.getMessage());
    }

    @ExceptionHandler(BadCredentialsException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public Map<String, String> handleBadCredentials(BadCredentialsException ex) {
        return Map.of("error", ex.getMessage());
    }

    @ExceptionHandler({
            CustomerAlreadyExistsException.class,
            CustomerAccountAlreadyExistsException.class
    })
    @ResponseStatus(HttpStatus.CONFLICT)
    public Map<String, String> handleConflict(RuntimeException ex) {
        return Map.of("error", ex.getMessage());
    }

    @ExceptionHandler({
            AnavailableServiceException.class,
            AppointmentCompletedException.class
    })
    @ResponseStatus(HttpStatus.UNPROCESSABLE_ENTITY)
    public Map<String, String> handleUnprocessable(RuntimeException ex) {
        return Map.of("error", ex.getMessage());
    }
}
