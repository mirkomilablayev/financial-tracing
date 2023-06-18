package com.finanacialtracing.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class NotFoundException extends RuntimeException {
    private final Errors error;

    public NotFoundException(Errors error) {
        super(error.getMessage());
        this.error = error;
    }

    public Errors getError() {
        return error;
    }
}
