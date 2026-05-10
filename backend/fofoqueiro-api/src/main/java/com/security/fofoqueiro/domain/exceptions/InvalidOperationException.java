package com.security.fofoqueiro.domain.exceptions;

public class InvalidOperationException extends RuntimeException {
    public InvalidOperationException(String message) {
        super(message);
    }
}
