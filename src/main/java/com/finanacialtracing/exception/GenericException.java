package com.finanacialtracing.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class GenericException extends RuntimeException {
    private final Errors error;

    public GenericException(Errors error) {
        super(error.getMessage());
        this.error = error;
    }

    public Errors getError() {
        return error;
    }
}
