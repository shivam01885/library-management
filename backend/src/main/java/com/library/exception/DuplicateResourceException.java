package com.library.exception;

// Signals a duplicate ISBN, email, or invalid repeated return operation.
public class DuplicateResourceException extends RuntimeException {

    // Creates a conflict exception with a useful message.
    public DuplicateResourceException(String message) {
        super(message);
    }
}
