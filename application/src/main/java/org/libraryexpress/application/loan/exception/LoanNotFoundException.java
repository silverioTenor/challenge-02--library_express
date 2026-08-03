package org.libraryexpress.application.loan.exception;

import org.libraryexpress.domain.exception.NotFoundException;

public class LoanNotFoundException extends NotFoundException {

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
