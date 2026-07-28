package org.libraryexpress.application.book.usecase;

import org.libraryexpress.application.book.dto.response.BookDto;
import org.libraryexpress.application.book.mapper.BookMapper;
import org.libraryexpress.domain.entity.Book;
import org.libraryexpress.domain.repository.BookRepository;
import org.libraryexpress.infrastructure.exception.NotFoundException;

import java.util.Optional;

public class FindBook {

    private final BookRepository bookRepository;
    private final BookMapper mapper;

    public FindBook(BookRepository bookRepository, BookMapper mapper) {
        this.bookRepository = bookRepository;
        this.mapper = mapper;
    }

    public BookDto execute(String ISBN) throws NotFoundException {

        Optional<Book> book = this.bookRepository.getByIsbn(ISBN);

        return book
                .map(mapper::toResponseDto)
                .orElseThrow(() -> new NotFoundException("Book not Found!"));
    }
}
