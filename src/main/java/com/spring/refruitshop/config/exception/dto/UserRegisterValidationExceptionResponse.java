package com.spring.refruitshop.config.exception.dto;

import lombok.Getter;

import java.time.LocalDateTime;
import java.util.Map;

@Getter
public class UserRegisterValidationExceptionResponse {

    private final String timestamp;
    private final int statusCode;
    private final String errorMessage;
    private final Map<String, String> errors;

    public UserRegisterValidationExceptionResponse(int statusCode, String errorMessage, Map<String, String> errors) {
        this.timestamp = LocalDateTime.now().toString();
        this.statusCode = statusCode;
        this.errorMessage = errorMessage;
        this.errors = errors;
    }
}
