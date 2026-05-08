package com.example.school_managementv1.exception;

public class BadRequestException extends RuntimeException {
    public BadRequestException(String message) { super(message); }
}
