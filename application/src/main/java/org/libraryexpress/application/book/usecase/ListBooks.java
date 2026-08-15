package org.libraryexpress.application.book.usecase;

import org.libraryexpress.application.book.dto.response.BookDto;
import org.libraryexpress.application.book.mapper.BookMapper;
import org.libraryexpress.domain.book.entity.Book;
import org.libraryexpress.domain.book.repository.BookRepository;

import java.util.Set;

public class ListBooks {

    private final BookRepository bookRepository;
    private final BookMapper mapper;

    public ListBooks(BookRepository bookRepository, BookMapper mapper) {
        this.bookRepository = bookRepository;
        this.mapper = mapper;
    }

    public Set<BookDto> execute() {
        var books = this.bookRepository.all();

        return mapper.toResponseListDto(books);
    }
}
