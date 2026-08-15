package org.libraryexpress.domain.customer.valueobject;

import org.libraryexpress.domain.customer.exception.InvalidEmailException;

import java.util.regex.Pattern;

public record Email(String value) {

    private static final Pattern EMAIL_PATTERN =
            Pattern.compile("^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$");

    public Email {
        if (value == null || value.isBlank())  throw new InvalidEmailException();

        if (!EMAIL_PATTERN.matcher(value).matches()) throw new InvalidEmailException("Invalid email format");
    }
}
