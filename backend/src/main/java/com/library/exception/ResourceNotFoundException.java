package com.library.exception;

// Signals that a requested database record does not exist.
public class ResourceNotFoundException extends RuntimeException {

    // Creates a not-found exception with a useful message.
    public ResourceNotFoundException(String message) {
        super(message);
    }
}
