package com.vinod.microservices.best.practices.util;

public enum ErrorCode {

    INTERNAL_ERROR(1001, "Internal Server Error occurred."),
    FAIL_TO_CALL_SERVICE(1002, "Fail to call service."),
    BAD_PARAMETER(1002, "Bad parameters."),

    USER_NOT_FOUND(2001, "This user does not exists."),
    DUPLICATE_USER(2002, "This user already exists."),
    USER_ACCOUNT_BLOCKED(2003, "This user account is being Blocked from Admin side.");


    private final int code;
    private final String description;

    private ErrorCode(int code, String description) {
        this.code = code;
        this.description = description;
    }

    public String getDescription() {
        return description;
    }

    public int getCode() {
        return code;
    }

    @Override
    public String toString() {
        return code + ": " + description;
    }
}
