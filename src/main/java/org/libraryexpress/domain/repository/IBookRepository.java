package org.libraryexpress.domain.repository;

import org.libraryexpress.domain.entity.Book;

import java.util.Optional;
import java.util.Set;

public interface IBookRepository {

    void create(Book book);
    Optional<Book> getByIsbn(String isbn);
    Optional<Set<Book>> all();
}
