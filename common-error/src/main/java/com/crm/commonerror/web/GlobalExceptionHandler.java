package com.crm.commonerror.web;

import com.crm.commonerror.core.AppException;
import com.crm.commonerror.core.ErrorCode;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.net.URI;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Optional;

@RestControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(AppException.class)
    public ResponseEntity<ProblemDetail> handleAppException(AppException ex, HttpServletRequest req) {
        ErrorCode ec = ex.getErrorCode();

        ProblemDetail pd = ProblemDetail.forStatusAndDetail(ec.getHttpStatus(), ex.getMessage());
        pd.setProperty("errorCode", ec.getCode());
        pd.setProperty("traceId", currentTraceId());
        pd.setInstance(URI.create(req.getRequestURI()));
        if (!ex.getDetails().isEmpty()) {
            pd.setProperty("details", ex.getDetails());
        }

        if (ec.getHttpStatus().is5xxServerError()) {
            log.error("AppException [{}] on {}: {}", ec.getCode(), req.getRequestURI(), ex.getMessage(), ex);
        } else {
            log.warn("AppException [{}] on {}: {}", ec.getCode(), req.getRequestURI(), ex.getMessage());
        }
        return ResponseEntity.status(ec.getHttpStatus()).body(pd);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ProblemDetail> handleValidation(MethodArgumentNotValidException ex, HttpServletRequest req) {
        Map<String, String> fieldErrors = new LinkedHashMap<>();
        for (FieldError fe : ex.getBindingResult().getFieldErrors()) {
            fieldErrors.put(fe.getField(), Optional.ofNullable(fe.getDefaultMessage()).orElse("invalid"));
        }

        ProblemDetail pd = ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST, "Ошибка валидации");
        pd.setProperty("errorCode", "VAL_400");
        pd.setProperty("traceId", currentTraceId());
        pd.setProperty("fields", fieldErrors);
        pd.setInstance(URI.create(req.getRequestURI()));

        log.warn("Validation failed on {}: {}", req.getRequestURI(), fieldErrors);
        return ResponseEntity.badRequest().body(pd);
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<ProblemDetail> handleDataIntegrity(DataIntegrityViolationException ex, HttpServletRequest req) {
        ProblemDetail pd = ProblemDetail.forStatusAndDetail(HttpStatus.CONFLICT,
                "Конфликт данных: запись уже существует или нарушает ограничение целостности");
        pd.setProperty("errorCode", "SYS_409");
        pd.setProperty("traceId", currentTraceId());
        pd.setInstance(URI.create(req.getRequestURI()));

        log.warn("DataIntegrityViolation on {}: {}", req.getRequestURI(), ex.getMessage());
        return ResponseEntity.status(HttpStatus.CONFLICT).body(pd);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ProblemDetail> handleUnknown(Exception ex, HttpServletRequest req) {
        ProblemDetail pd = ProblemDetail.forStatusAndDetail(HttpStatus.INTERNAL_SERVER_ERROR, "Внутренняя ошибка сервиса");
        pd.setProperty("errorCode", "SYS_500");
        pd.setProperty("traceId", currentTraceId());
        pd.setInstance(URI.create(req.getRequestURI()));

        log.error("Unhandled exception on {}", req.getRequestURI(), ex);
        return ResponseEntity.internalServerError().body(pd);
    }

    private String currentTraceId() {
        String traceId = MDC.get("traceId");
        return traceId != null ? traceId : "n/a";
    }
}