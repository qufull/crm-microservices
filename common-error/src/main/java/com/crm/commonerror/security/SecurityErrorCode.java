package com.crm.commonerror.security;

import com.crm.commonerror.core.ErrorCode;
import org.springframework.http.HttpStatus;

public enum SecurityErrorCode implements ErrorCode {
    TOKEN_MISSING("SEC_401_1", HttpStatus.UNAUTHORIZED, "Токен не предоставлен"),
    TOKEN_EXPIRED("SEC_401_2", HttpStatus.UNAUTHORIZED, "Токен истёк"),
    TOKEN_INVALID("SEC_401_3", HttpStatus.UNAUTHORIZED, "Невалидный токен"),
    ACCESS_DENIED("SEC_403_1", HttpStatus.FORBIDDEN, "Недостаточно прав для этого действия");

    private final String code;
    private final HttpStatus httpStatus;
    private final String defaultMessage;

    SecurityErrorCode(String code, HttpStatus httpStatus, String defaultMessage) {
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