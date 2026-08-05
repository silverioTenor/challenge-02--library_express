package org.libraryexpress.application.loan.exception;

public class SearchLoanValidationException extends Exception {

    public SearchLoanValidationException() {
        super("At least one search criteria must be provided");
    }

    public SearchLoanValidationException(String message) {
        super(message);
    }

    public SearchLoanValidationException(String message, Throwable cause) {
        super(message, cause);
    }

    public SearchLoanValidationException(Throwable cause) {
        super(cause);
    }
}
