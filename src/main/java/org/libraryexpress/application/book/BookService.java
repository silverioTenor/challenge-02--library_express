package org.libraryexpress.application.book;

import org.libraryexpress.domain.entity.Book;
import org.libraryexpress.domain.repository.IBookRepository;
import org.libraryexpress.infrastructure.exception.BookUnavailableException;
import org.libraryexpress.infrastructure.repository.BookRepository;

import java.util.Scanner;
import java.util.Set;

public class BookService {

    private final IBookRepository bookRepository;

    public BookService() {
        this.bookRepository = BookRepository.DB;
    }

    public Book register() {

        Book book = new Book.Builder()
                .setTitle(title)
                .setAuthor(author)
                .setYear(year)
                .build();

        scan.close();

        this.bookRepository.create(book);
        return book;
    }

    public Book findByIsbn(String isbn) throws BookUnavailableException {
        var result = this.bookRepository.getByIsbn(isbn);

        if (result.isEmpty()) throw new BookUnavailableException("Book unavailable!");

        return result.get();
    }

    public Set<Book> findAll() {
        return this.bookRepository.all().orElse(null);
//        return book.orElseGet(HashSet::new);
    }
}
