package org.libraryexpress.application.book.usecase;

import org.libraryexpress.application.book.dto.request.RegisterBookDto;
import org.libraryexpress.application.book.mapper.BookMapper;
import org.libraryexpress.domain.entity.Book;
import org.libraryexpress.domain.repository.IBookRepository;
import org.libraryexpress.infrastructure.exception.RuleViolationException;
import org.libraryexpress.infrastructure.repository.BookRepository;

public class RegisterBook {

    private final IBookRepository bookRepository;
    private final BookMapper mapper;

    public RegisterBook() {
        this.bookRepository = BookRepository.DB;
        this.mapper = BookMapper.INSTANCE;
    }

    public void execute(RegisterBookDto registerBookDto) throws RuleViolationException {

        var hasRegistered = this.bookRepository.getByIsbn(registerBookDto.ISBN());

        if (hasRegistered.isPresent()) throw new RuleViolationException("Cannot register the same book");

        Book book = this.mapper.toEntity(registerBookDto).build();

        this.bookRepository.create(book);
    }
}
