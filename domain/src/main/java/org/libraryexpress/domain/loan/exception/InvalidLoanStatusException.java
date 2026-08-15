package org.libraryexpress.domain.loan.exception;

import org.libraryexpress.domain.core.DomainException;

public class InvalidLoanStatusException extends DomainException {

    public InvalidLoanStatusException() {
    }

    public InvalidLoanStatusException(String message) {
        super(message);
    }

    public InvalidLoanStatusException(String message, Throwable cause) {
        super(message, cause);
    }

    public InvalidLoanStatusException(Throwable cause) {
        super(cause);
    }
}
