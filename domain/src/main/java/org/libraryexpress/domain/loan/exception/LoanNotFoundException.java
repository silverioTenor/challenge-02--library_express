package org.libraryexpress.domain.loan.exception;

import org.libraryexpress.domain.core.DomainException;

public class LoanNotFoundException extends DomainException {

    public LoanNotFoundException() {
        super("Loan not found!");
    }

    public LoanNotFoundException(String message) {
        super(message);
    }

    public LoanNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}
