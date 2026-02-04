package com.anshu.device_registration.exception;

import java.time.LocalDateTime;
import java.time.ZoneOffset;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    // ================== DEVICE EXCEPTIONS ==================

    @ExceptionHandler(DeviceNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleDeviceNotFound(DeviceNotFoundException ex) {
        return build(ex.getMessage(), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(InvalidDeviceSecretException.class)
    public ResponseEntity<ErrorResponse> handleInvalidSecret(InvalidDeviceSecretException ex) {
        return build(ex.getMessage(), HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(DeviceInactiveException.class)
    public ResponseEntity<ErrorResponse> handleDeviceInactive(DeviceInactiveException ex) {
        return build(ex.getMessage(), HttpStatus.FORBIDDEN);
    }

    // ================== ADMIN / DASHBOARD EXCEPTIONS ==================

    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleUserNotFound(UserNotFoundException ex) {
        return build(ex.getMessage(), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler({AccessDeniedException.class, RoleMismatchException.class})
    public ResponseEntity<ErrorResponse> handleAccessDenied(RuntimeException ex) {
        return build(ex.getMessage(), HttpStatus.FORBIDDEN);
    }

    // ================== GENERIC EXCEPTION ==================

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleGeneric(Exception ex) {
        ex.printStackTrace(); // log stack trace for debugging
        return build("Internal Server Error", HttpStatus.INTERNAL_SERVER_ERROR);
    }

    // ================== PRIVATE BUILDER ==================

    private ResponseEntity<ErrorResponse> build(String msg, HttpStatus status) {
        return ResponseEntity.status(status)
                .body(
                        ErrorResponse.builder()
                                .message(msg)
                                .status(status.value())
                                .timestamp(LocalDateTime.now(ZoneOffset.UTC))
                                .build()
                );
    }
}
