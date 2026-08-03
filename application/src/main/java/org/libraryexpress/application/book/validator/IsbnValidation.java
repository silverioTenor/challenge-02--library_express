package org.libraryexpress.application.book.validator;

import org.libraryexpress.application.book.exception.NullIsbnException;
import org.libraryexpress.domain.validator.Validator;

public class IsbnValidation implements Validator<String> {

    @Override
    public void validate(String ISBN) throws NullIsbnException {

        if (ISBN == null || ISBN.isBlank()) {
            throw new NullIsbnException("ISBN cannot be empty.");
        }
    }
}
