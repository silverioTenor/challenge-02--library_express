package org.libraryexpress.application.book.exception;

import org.libraryexpress.domain.exception.RuleViolationException;

public class NullIsbnException extends RuleViolationException {

    public NullIsbnException() {
        super("ISBN cannot be null/empty.");
    }

    public NullIsbnException(String message) {
        super(message);
    }

    public NullIsbnException(String message, Throwable cause) {
        super(message, cause);
    }

    public NullIsbnException(Throwable cause) {
        super(cause);
    }
}
