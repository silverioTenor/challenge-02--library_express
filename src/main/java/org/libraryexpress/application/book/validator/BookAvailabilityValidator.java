package org.libraryexpress.application.book.validator;

import org.libraryexpress.domain.entity.Book;
import org.libraryexpress.domain.enums.BookStatus;
import org.libraryexpress.domain.repository.IBookRepository;
import org.libraryexpress.domain.validator.IValidator;
import org.libraryexpress.infrastructure.exception.NotFoundException;
import org.libraryexpress.infrastructure.exception.RuleViolationException;
import org.libraryexpress.infrastructure.repository.BookRepository;

import java.util.Set;

public class BookAvailabilityValidator implements IValidator<String> {

    private final IBookRepository bookRepository;

    public BookAvailabilityValidator() {
        this.bookRepository = BookRepository.DB;
    }

    @Override
    public void validate(String ISBN) throws NotFoundException, RuleViolationException {

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
