package org.libraryexpress.domain.book.exception;

import org.libraryexpress.domain.core.DomainException;

public class BookNotFoundException extends DomainException {

    public BookNotFoundException() {
        super("Book not Found!");
    }

    public BookNotFoundException(String message) {
        super(message);
    }

    public BookNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}
