package org.libraryexpress.application.book.usecase;

import org.libraryexpress.domain.entity.Book;
import org.libraryexpress.domain.repository.IBookRepository;
import org.libraryexpress.infrastructure.repository.BookRepository;

import java.util.Set;

public class ListBooks {

    private final IBookRepository bookRepository;

    public ListBooks(IBookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }

    public Set<Book> execute() {
        return this.bookRepository.all().orElse(null);
    }
}
