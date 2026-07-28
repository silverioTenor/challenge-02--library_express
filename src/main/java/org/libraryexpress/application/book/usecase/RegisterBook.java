package org.libraryexpress.application.book.usecase;

import org.libraryexpress.application.book.dto.request.RegisterBookDto;
import org.libraryexpress.application.book.mapper.BookMapper;
import org.libraryexpress.domain.entity.Book;
import org.libraryexpress.domain.repository.BookRepository;
import org.libraryexpress.infrastructure.exception.RuleViolationException;

public class RegisterBook {

    private final BookRepository bookRepository;
    private final BookMapper mapper;

    public RegisterBook(BookRepository bookRepository, BookMapper mapper) {
        this.bookRepository = bookRepository;
        this.mapper = mapper;
    }

    public void execute(RegisterBookDto registerBookDto) throws RuleViolationException {

        var hasRegistered = this.bookRepository.getByIsbn(registerBookDto.ISBN());

        if (hasRegistered.isPresent()) throw new RuleViolationException("Cannot register the same book");

        Book book = this.mapper.toEntity(registerBookDto).build();

        this.bookRepository.create(book);
    }
}
