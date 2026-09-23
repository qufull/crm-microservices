package com.crm.commonlib.security.exception;

import com.crm.commonerror.security.SecurityErrorCode;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.oauth2.server.resource.InvalidBearerTokenException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class JwtAuthenticationEntryPoint implements AuthenticationEntryPoint {

    private static final Logger log = LoggerFactory.getLogger(JwtAuthenticationEntryPoint.class);
    private final ObjectMapper objectMapper;

    public JwtAuthenticationEntryPoint(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response,
                         AuthenticationException authException) throws IOException {

        SecurityErrorCode errorCode = resolveErrorCode(authException);
        log.warn("Authentication failed on {}: {}", request.getRequestURI(), authException.getMessage());
        ProblemDetailWriter.write(request, response, objectMapper, errorCode);
    }

    private SecurityErrorCode resolveErrorCode(AuthenticationException authException) {
        Throwable cause = authException.getCause();
        boolean isBearerIssue = authException instanceof InvalidBearerTokenException
                || cause instanceof InvalidBearerTokenException;

        if (isBearerIssue) {
            String detail = authException.getMessage() != null ? authException.getMessage() : "";
            return detail.toLowerCase().contains("expired")
                    ? SecurityErrorCode.TOKEN_EXPIRED
                    : SecurityErrorCode.TOKEN_INVALID;
        }

        if (authException.getMessage() != null && authException.getMessage().contains("Full authentication")) {
            return SecurityErrorCode.TOKEN_MISSING;
        }

        return SecurityErrorCode.TOKEN_INVALID;
    }
}