package org.libraryexpress.domain.loan.exception;

import org.libraryexpress.domain.core.DomainException;

public class OverdueLoanException extends DomainException {

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
