package org.libraryexpress.domain.book.exception;

import org.libraryexpress.domain.core.DomainException;

public class UniqueIsbnViolationException extends DomainException {

    public UniqueIsbnViolationException() {
        super("It is not permitted to register a book with an ISBN that is already in use.");
    }

    public UniqueIsbnViolationException(String message) {
        super(message);
    }

    public UniqueIsbnViolationException(String message, Throwable cause) {
        super(message, cause);
    }

    public UniqueIsbnViolationException(Throwable cause) {
        super(cause);
    }
}
