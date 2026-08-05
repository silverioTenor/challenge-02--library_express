package org.libraryexpress.application.book.dto.response;

import org.libraryexpress.domain.book.enums.BookStatus;

public record BookDto(
        String ISBN,
        String title,
        String author,
        int year,
        BookStatus status
) {
}
