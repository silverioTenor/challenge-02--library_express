package org.libraryexpress.application.customer.exception;

import org.libraryexpress.domain.exception.RuleViolationException;

public class UniqueEmailException extends RuleViolationException {

    public UniqueEmailException() {
        super("E-mail must be unique.");
    }

    public UniqueEmailException(String message) {
        super(message);
    }

    public UniqueEmailException(String message, Throwable cause) {
        super(message, cause);
    }

    public UniqueEmailException(Throwable cause) {
        super(cause);
    }
}
