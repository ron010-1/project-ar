package com.rampex.paymentapi.dto;

import java.time.Instant;
import java.util.Map;

public record ErrorResponseDTO(
        Instant timestamp,
        int status,
        String error,
        String message,
        Map<String, String> fieldErrors
) {
    public static ErrorResponseDTO of(int status, String error, String message) {
        return new ErrorResponseDTO(Instant.now(), status, error, message, null);
    }

    public static ErrorResponseDTO of(int status, String error, String message, Map<String, String> fieldErrors) {
        return new ErrorResponseDTO(Instant.now(), status, error, message, fieldErrors);
    }
}
