package org.libraryexpress.application.loan.exception;

import org.libraryexpress.domain.exception.RuleViolationException;

public class InvalidLoanStatusException extends RuleViolationException {

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
