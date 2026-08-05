package org.libraryexpress.domain.book.exception;

import org.libraryexpress.domain.core.DomainException;

public class BookUnavailableException extends DomainException {

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
