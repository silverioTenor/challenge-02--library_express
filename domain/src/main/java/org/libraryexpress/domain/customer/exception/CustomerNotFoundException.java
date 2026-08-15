package org.libraryexpress.domain.customer.exception;

import org.libraryexpress.domain.core.DomainException;

public class CustomerNotFoundException extends DomainException {

    public CustomerNotFoundException() {
        super("Customer not found!");
    }

    public CustomerNotFoundException(String message) {
        super(message);
    }

    public CustomerNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}
