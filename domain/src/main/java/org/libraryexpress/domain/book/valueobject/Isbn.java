package org.libraryexpress.domain.book.valueobject;

import org.libraryexpress.domain.book.exception.InvalidIsbnException;

import java.util.Random;
import java.util.regex.Pattern;

public record Isbn(String value) {

    private static final Pattern ISBN_PATTERN = Pattern.compile("^\\d{3}-\\d{2}-\\d{5}-\\d{2}-\\d{1}$");


    public Isbn {
        if (value == null || value.isBlank()) throw new InvalidIsbnException();

        if (!ISBN_PATTERN.matcher(value).matches()) throw new InvalidIsbnException();
    }

    public static Isbn generate() {
        Random random = new Random();

        String generatedString = String.format("%03d-%02d-%05d-%02d-%d",
                random.nextInt(1000),
                random.nextInt(100),
                random.nextInt(100000),
                random.nextInt(100),
                random.nextInt(10));

        return new Isbn(generatedString);
    }
}
