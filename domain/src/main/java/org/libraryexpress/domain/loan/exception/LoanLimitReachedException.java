package org.libraryexpress.domain.loan.exception;

import org.libraryexpress.domain.core.DomainException;

public class LoanLimitReachedException extends DomainException {

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
