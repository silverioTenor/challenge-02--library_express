package org.libraryexpress.domain.book.exception;

import org.libraryexpress.domain.core.DomainException;

public class InvalidIsbnException extends DomainException {

    public InvalidIsbnException() {
        super("The ISBN cannot be null or empty.");
    }

    public InvalidIsbnException(String message) {
        super(message);
    }

    public InvalidIsbnException(String message, Throwable cause) {
        super(message, cause);
    }

    public InvalidIsbnException(Throwable cause) {
        super(cause);
    }
}
