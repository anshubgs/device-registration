package com.anshu.device_registration.exception;

import java.time.LocalDateTime;
import java.time.ZoneOffset;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(DeviceNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleNotFound(
            DeviceNotFoundException ex) {
        return build(ex.getMessage(), HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(InvalidDeviceSecretException.class)
    public ResponseEntity<ErrorResponse> handleSecret(
            InvalidDeviceSecretException ex) {
        return build(ex.getMessage(), HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(DeviceInactiveException.class)
    public ResponseEntity<ErrorResponse> handleInactive(
            DeviceInactiveException ex) {
        return build(ex.getMessage(), HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleGeneric(
            Exception ex) {
        return build("Internal Server Error",
                HttpStatus.INTERNAL_SERVER_ERROR);
    }

    private ResponseEntity<ErrorResponse> build(
            String msg, HttpStatus status) {

        return ResponseEntity.status(status)
                .body(
                        ErrorResponse.builder()
                                .message(msg)
                                .status(status.value())
                                .timestamp(
                                        LocalDateTime.now(ZoneOffset.UTC))
                                .build());
    }
}
