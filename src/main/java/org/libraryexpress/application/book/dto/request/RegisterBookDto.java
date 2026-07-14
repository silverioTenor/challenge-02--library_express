package org.libraryexpress.application.book.dto.request;

import org.libraryexpress.domain.enums.BookStatus;

public record RegisterBookDto(
        String ISBN,
        String title,
        String author,
        int year,
        BookStatus status
) {
}
