package org.libraryexpress.application.loan.exception;

import org.libraryexpress.domain.exception.RuleViolationException;

public class LoanLimitReachedException extends RuleViolationException {

    public LoanLimitReachedException() {
    }

    public LoanLimitReachedException(String message) {
        super(message);
    }

    public LoanLimitReachedException(String message, Throwable cause) {
        super(message, cause);
    }

    public LoanLimitReachedException(Throwable cause) {
        super(cause);
    }
}
