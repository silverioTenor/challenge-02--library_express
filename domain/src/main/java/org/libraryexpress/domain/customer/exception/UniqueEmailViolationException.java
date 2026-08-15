package org.libraryexpress.domain.customer.exception;

import org.libraryexpress.domain.core.DomainException;

public class UniqueEmailViolationException extends DomainException {

    public UniqueEmailViolationException() {
        super("E-mail must be unique.");
    }

    public UniqueEmailViolationException(String message) {
        super(message);
    }

    public UniqueEmailViolationException(String message, Throwable cause) {
        super(message, cause);
    }

    public UniqueEmailViolationException(Throwable cause) {
        super(cause);
    }
}
