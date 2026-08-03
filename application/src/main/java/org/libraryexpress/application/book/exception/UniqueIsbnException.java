package org.libraryexpress.application.book.exception;

import org.libraryexpress.domain.exception.RuleViolationException;

public class UniqueIsbnException extends RuleViolationException {

    public UniqueIsbnException() {
        super("It is not permitted to register a book with an ISBN that is already in use.");
    }

    public UniqueIsbnException(String message) {
        super(message);
    }

    public UniqueIsbnException(String message, Throwable cause) {
        super(message, cause);
    }

    public UniqueIsbnException(Throwable cause) {
        super(cause);
    }
}
