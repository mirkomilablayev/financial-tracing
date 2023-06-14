package com.finanacialtracing.exception;

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
    CANNOT_DISABLE_FO(11, "Cannot delete Financial Operation"),
    CANNOT_CREATE_ORGANIZATION_POSITION(12, "Cannot create organization position"),
    CANNOT_DELETE_ORGANIZATION_POSITION(13, "Cannot delete organization worker position"),
    CANNOT_GET_WORKER_POSITIONS(14, "Cannot get worker position"),
    CANNOT_UPDATE_ORGANIZATION(15, "Cannot update organization"),
    CANNOT_DELETE_ORGANIZATION(16, "Cannot delete organization"),
    USER_ALREADY_ADDED_AS_WORKER(17, "This user is already added as worker"),
    CANNOT_CREATE_WORKER_CAUSE_WORKER_PERMISSION(18, "Cannot create worker because there is no permission"),
    CANNOT_DELETE_WORKER(19, "Cannot delete worker"),
    CANNOT_UPDATE_WORKER(20, "Cannot update worker"),
    CANNOT_GET_WORKER(21, "Cannot get organization workers"),
    CANNOT_CREATE_FO_TYPE(22, "Cannot create financial operation type"),
    CANNOT_GET_FO_TYPE_LIST(23, "Cannot get fo type list"),
    YOU_CANNOT_ADD_FINANCIAL_OPERATION(24, "You cannot add financial operation cause you have not access"),
    CANNOT_ADD_USER_AS_ADMIN(25, "Cannot add this user as admin"),
    USER_ALREADY_ADMIN(26, "This user is already added as admin"),
    CANNOT_DELETE_ADMIN(27, "Cannot delete admin"),
    ACCESS_DENIED(28, "Access denied"),
    CANNOT_UPDATE_FO_FULL_NAME(29, "Cannot update fullname"),
    CANNOT_CHANGE_PASSWORD(30, "Cannot change password");
    private final Integer code;
    private final String message;

    Errors(Integer code, String message) {
        this.code = code;
        this.message = message;
    }
}
