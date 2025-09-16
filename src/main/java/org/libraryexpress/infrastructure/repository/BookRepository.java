package org.libraryexpress.infrastructure.repository;

import org.libraryexpress.domain.entity.Book;
import org.libraryexpress.domain.repository.IBookRepository;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.TreeSet;

public enum BookRepository implements IBookRepository {
    DB;

    private final Set<Book> group = new HashSet<>();

    @Override
    public void create(Book book) {
        group.add(book);
    }

    @Override
    public Optional<Book> getByIsbn(String isbn) {
        return group.stream()
                .filter(book -> book.ISBN.equals(isbn))
                .findFirst();
    }

    @Override
    public Optional<Set<Book>> all() {
        return Optional.of(group);
    }
}
