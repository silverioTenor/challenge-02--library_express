package org.libraryexpress.application.book.helper;

import org.libraryexpress.domain.entity.Book;
import org.libraryexpress.domain.enums.BookStatus;
import org.libraryexpress.domain.repository.IBookRepository;
import org.libraryexpress.infrastructure.exception.NotFoundException;
import org.libraryexpress.infrastructure.exception.RuleViolationException;
import org.libraryexpress.infrastructure.repository.BookRepository;

import java.util.Set;

public class BookAvailability {

    private final IBookRepository bookRepository;

    public BookAvailability(IBookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }

    public void check(String ISBN) throws NotFoundException, RuleViolationException {

        if (ISBN == null || ISBN.isBlank()) {
            throw new RuleViolationException("ISBN cannot be empty.");
        }

        Set<Book> books = this.bookRepository.search(ISBN, null);

        Book book = books.stream()
                .findFirst()
                .orElseThrow(() -> new NotFoundException("Book not found!"));

        if (book.getStatus() != BookStatus.AVAILABLE) {
            throw new RuleViolationException("Book is not available.");
        }
    }
}
