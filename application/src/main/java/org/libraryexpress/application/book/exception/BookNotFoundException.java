package org.libraryexpress.application.book.exception;

import org.libraryexpress.domain.exception.NotFoundException;

public class BookNotFoundException extends NotFoundException {

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
