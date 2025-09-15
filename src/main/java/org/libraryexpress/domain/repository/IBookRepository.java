package org.libraryexpress.domain.repository;

import org.libraryexpress.domain.entity.Book;

import java.util.List;
import java.util.Optional;

public interface IBookRepository {

    Optional<Book> getByIsbn(String isbn);
    Optional<List<Book>> all();
}
