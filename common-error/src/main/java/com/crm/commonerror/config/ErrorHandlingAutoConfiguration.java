package com.crm.commonerror.config;

import com.crm.commonerror.web.GlobalExceptionHandler;
import com.crm.commonerror.web.TraceIdFilter;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.context.annotation.Import;

@AutoConfiguration
@Import({GlobalExceptionHandler.class, TraceIdFilter.class})
public class ErrorHandlingAutoConfiguration {
}