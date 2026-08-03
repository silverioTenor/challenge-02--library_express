package org.libraryexpress.application.loan.exception;

import org.libraryexpress.domain.exception.RuleViolationException;

public class OverdueLoanException extends RuleViolationException {

    public OverdueLoanException() {
        super("Only overdue loans can be closed through this flow.");
    }

    public OverdueLoanException(String message) {
        super(message);
    }

    public OverdueLoanException(String message, Throwable cause) {
        super(message, cause);
    }

    public OverdueLoanException(Throwable cause) {
        super(cause);
    }
}
