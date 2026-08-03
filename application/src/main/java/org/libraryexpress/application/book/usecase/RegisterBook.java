package org.libraryexpress.application.book.usecase;

import org.libraryexpress.application.book.dto.request.RegisterBookDto;
import org.libraryexpress.application.book.exception.UniqueIsbnException;
import org.libraryexpress.application.book.mapper.BookMapper;
import org.libraryexpress.domain.entity.Book;
import org.libraryexpress.domain.repository.BookRepository;

public class RegisterBook {

    private final BookRepository bookRepository;
    private final BookMapper mapper;

    public RegisterBook(BookRepository bookRepository, BookMapper mapper) {
        this.bookRepository = bookRepository;
        this.mapper = mapper;
    }

    public void execute(RegisterBookDto registerBookDto) throws UniqueIsbnException {

        var hasRegistered = this.bookRepository.getByIsbn(registerBookDto.ISBN());

        if (hasRegistered.isPresent()) throw new UniqueIsbnException();

        Book book = this.mapper.toEntity(registerBookDto).build();

        this.bookRepository.create(book);
    }
}
