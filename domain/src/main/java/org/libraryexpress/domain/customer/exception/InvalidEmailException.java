package org.libraryexpress.domain.customer.exception;

import org.libraryexpress.domain.core.DomainException;

public class InvalidEmailException extends DomainException {

    public InvalidEmailException() {
        super("The email cannot be null or empty.");
    }

    public InvalidEmailException(String message) {
        super(message);
    }

    public InvalidEmailException(String message, Throwable cause) {
        super(message, cause);
    }

    public InvalidEmailException(Throwable cause) {
        super(cause);
    }
}
