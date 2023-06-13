package com.finanacialtracing.financialtracingapp.exception;

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
