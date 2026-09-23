package com.crm.commonlib.security.exception;

import com.crm.commonerror.core.ErrorCode;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.MediaType;
import org.springframework.http.ProblemDetail;

import java.io.IOException;
import java.net.URI;

final class ProblemDetailWriter {

    private ProblemDetailWriter() {
    }

    static void write(HttpServletRequest request, HttpServletResponse response,
                      ObjectMapper objectMapper, ErrorCode errorCode) throws IOException {
        ProblemDetail pd = ProblemDetail.forStatusAndDetail(errorCode.getHttpStatus(), errorCode.getDefaultMessage());
        pd.setProperty("errorCode", errorCode.getCode());
        pd.setInstance(URI.create(request.getRequestURI()));

        response.setStatus(errorCode.getHttpStatus().value());
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.getWriter().write(objectMapper.writeValueAsString(pd));
    }
}