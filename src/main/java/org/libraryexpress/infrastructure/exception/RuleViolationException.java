package org.libraryexpress.infrastructure.exception;

public class RuleViolationException extends Exception {
    public RuleViolationException() {
    }

    public RuleViolationException(String message) {
        super(message);
    }

    public RuleViolationException(String message, Throwable cause) {
        super(message, cause);
    }

    public RuleViolationException(Throwable cause) {
        super(cause);
    }
}
