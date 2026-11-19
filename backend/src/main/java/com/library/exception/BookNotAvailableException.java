package com.library.exception;

// Signals that no copy of a requested book is available to issue.
public class BookNotAvailableException extends RuntimeException {

    // Creates an availability exception with a useful message.
    public BookNotAvailableException(String message) {
        super(message);
    }
}
