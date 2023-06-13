package com.finanacialtracing.financialtracingapp.exception;

import lombok.Getter;

@Getter
public enum Errors {
    SUCCESS(0, "Success"),
    UNKNOWN(1, "Unknown error"),
    USERNAME_ALREADY_TAKEN(2, "Username already taken"),
    USER_ROLE_NOT_FOUND(3, "Username not found"),
    USERNAME_VALIDATION_ERROR(4, "Username must start with a letter and can contain A-Z, a-z, 0-9, -, . with a minimum length of 5"),
    NULL_PARAMS_ERROR(5, "Null params input from client"),
    ORGANIZATION_NOT_FOUND(6, "Organization not found"),
    NOT_FOUND(7, "Not Found"),
    CANNOT_UPDATE_FO_TYPE(8, "Cannot update fo type"),
    CANNOT_DELETE_FO_TYPE(9, "Cannot delete Financial Operation Type"),
    FORBIDDEN(10, "Forbidden"),
    CANNOT_DISABLE_FO(11, "Cannot delete Financial Operation");
    private final Integer code;
    private final String message;

    Errors(Integer code, String message) {
        this.code = code;
        this.message = message;
    }
}
