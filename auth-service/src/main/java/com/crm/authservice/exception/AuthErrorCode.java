package com.crm.authservice.exception;

import com.crm.commonerror.core.ErrorCode;
import org.springframework.http.HttpStatus;

public enum AuthErrorCode implements ErrorCode {
    USER_ALREADY_EXISTS("AUTH_409_1", HttpStatus.CONFLICT, "Пользователь с таким email уже существует"),
    INVALID_CREDENTIALS("AUTH_401_1", HttpStatus.UNAUTHORIZED, "Неверный email или пароль");

    private final String code;
    private final HttpStatus httpStatus;
    private final String defaultMessage;

    AuthErrorCode(String code, HttpStatus httpStatus, String defaultMessage) {
        this.code = code;
        this.httpStatus = httpStatus;
        this.defaultMessage = defaultMessage;
    }

    @Override
    public String getCode() {
        return code;
    }

    @Override
    public HttpStatus getHttpStatus() {
        return httpStatus;
    }

    @Override
    public String getDefaultMessage() {
        return defaultMessage;
    }
}