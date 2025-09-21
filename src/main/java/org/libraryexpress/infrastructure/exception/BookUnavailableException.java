package org.libraryexpress.infrastructure.exception;

public class BookUnavailableException extends Exception {

    public BookUnavailableException(String message) {
        super(message);
    }

    public BookUnavailableException(String message, Throwable cause) {
        super(message, cause);
    }
}
