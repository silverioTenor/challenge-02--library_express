package org.libraryexpress.application.book.dto;

public record RegisterBookDto(
        String ISBN,
        String title,
        String author,
        int year
) {
}
