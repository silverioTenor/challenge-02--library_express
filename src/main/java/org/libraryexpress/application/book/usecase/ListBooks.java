package org.libraryexpress.application.book.usecase;

import org.libraryexpress.application.book.mapper.BookMapper;
import org.libraryexpress.domain.entity.Book;
import org.libraryexpress.domain.repository.IBookRepository;
import org.libraryexpress.infrastructure.repository.BookRepository;

import java.util.Set;

public class ListBooks {

    private final IBookRepository bookRepository;
    private final BookMapper mapper;

    public ListBooks(IBookRepository bookRepository, BookMapper mapper) {
        this.bookRepository = bookRepository;
        this.mapper = mapper;
    }

    public Set<Book> execute() {
        return this.bookRepository.all().orElse(null);
    }
}
