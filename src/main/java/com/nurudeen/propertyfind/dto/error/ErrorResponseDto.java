package com.nurudeen.propertyfind.dto.error;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.Map;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class ErrorResponseDto {
    private int status;
    private String message;
    private Map<String, String> errors; // only present for validation errors

    // Standard error (404, 409, 500, etc.)
    public ErrorResponseDto(int status, String message) {
        this.status = status;
        this.message = message;
    }

    // Validation error (400 with field-level details)
    public ErrorResponseDto(int status, String message, Map<String, String> errors) {
        this.status = status;
        this.message = message;
        this.errors = errors;
    }

    public int getStatus() { return status; }
    public String getMessage() { return message; }
    public Map<String, String> getErrors() { return errors; }
}
