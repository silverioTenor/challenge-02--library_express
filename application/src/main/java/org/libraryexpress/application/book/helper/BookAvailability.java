package org.libraryexpress.application.book.helper;

import org.libraryexpress.application.book.exception.BookNotFoundException;
import org.libraryexpress.application.book.exception.BookUnavailableException;
import org.libraryexpress.application.book.exception.NullIsbnException;
import org.libraryexpress.application.book.validator.IsbnValidation;
import org.libraryexpress.domain.entity.Book;
import org.libraryexpress.domain.enums.BookStatus;
import org.libraryexpress.domain.repository.BookRepository;

import java.util.Set;

public class BookAvailability {

    private final BookRepository bookRepository;
    private final IsbnValidation isbnValidation;

    public BookAvailability(
            BookRepository bookRepository,
            IsbnValidation isbnValidation
    ) {
        this.bookRepository = bookRepository;
        this.isbnValidation = isbnValidation;
    }

    public void check(String ISBN) throws BookNotFoundException, BookUnavailableException {

        try {
            this.isbnValidation.validate(ISBN);
        } catch (NullIsbnException e) {
            throw new BookNotFoundException();
        }

        Set<Book> books = this.bookRepository.search(ISBN, null);

        Book book = books.stream()
                .findFirst()
                .orElseThrow(BookNotFoundException::new);

        if (book.getStatus() != BookStatus.AVAILABLE) {
            throw new BookUnavailableException();
        }
    }
}
