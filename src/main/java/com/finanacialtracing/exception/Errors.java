package com.finanacialtracing.exception;

import lombok.Getter;

@Getter
public enum Errors {
    SUCCESS(0, "Success"),
    UNKNOWN(1, "Unknown error"),
    USERNAME_ALREADY_TAKEN(2, "Username already taken"),
    USER_ROLE_NOT_FOUND(3, "Username not found"),
    USERNAME_VALIDATION_ERROR(4, "Username must start with a letter and can contain A-Z, a-z, 0-9, -, . with a minimum length of 5"),
    NOT_FOUND(7, "Not Found"),
    FORBIDDEN(10, "Forbidden"),
    USER_ALREADY_ADDED_AS_WORKER(17, "This user is already added as worker"),
    USER_ALREADY_ADMIN(26, "This user is already added as admin"),
    ACCESS_DENIED(28, "Access denied"),
    CANNOT_CHANGE(31, "Cannot change"),
    CANNOT_CREATE(31, "Cannot create"),
    CANNOT_ADD(31, "Cannot add"),
    CANNOT_DELETE(31, "Cannot delete"),
    CANNOT_UPDATE(31, "Cannot update"),
    CANNOT_GET(31, "Cannot get")
    ;
    private final Integer code;
    private final String message;

    Errors(Integer code, String message) {
        this.code = code;
        this.message = message;
    }
}
