package org.libraryexpress.application.customer.exception;

import org.libraryexpress.domain.exception.NotFoundException;

public class CustomerNotFoundException extends NotFoundException {

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
