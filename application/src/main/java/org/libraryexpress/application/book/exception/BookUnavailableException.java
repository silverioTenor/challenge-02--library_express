package org.libraryexpress.application.book.exception;

import org.libraryexpress.domain.exception.RuleViolationException;

public class BookUnavailableException extends RuleViolationException {

    public BookUnavailableException() {
        super("Book is not available.");
    }

    public BookUnavailableException(String message) {
        super(message);
    }

    public BookUnavailableException(String message, Throwable cause) {
        super(message, cause);
    }

    public BookUnavailableException(Throwable cause) {
        super(cause);
    }
}
