package org.libraryexpress.application.book.usecase;

import org.libraryexpress.application.book.dto.RegisterBookDto;
import org.libraryexpress.domain.repository.IBookRepository;
import org.libraryexpress.infrastructure.repository.BookRepository;

public class RegisterBook {

    private final IBookRepository bookRepository;

    public RegisterBook() {
        this.bookRepository = BookRepository.DB;
    }

    public void execute(RegisterBookDto registerBookDto) {

        var foundBook = this.bookRepository.
    }
}
