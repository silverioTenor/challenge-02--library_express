package org.libraryexpress.infrastructure.repository;

import org.libraryexpress.domain.entity.Book;
import org.libraryexpress.domain.enums.BookStatus;
import org.libraryexpress.domain.repository.IBookRepository;
import org.libraryexpress.infrastructure.exception.NotFoundException;

import java.util.HashSet;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public enum BookRepository implements IBookRepository {
    DB;

    private final Set<Book> group = new HashSet<>();

    @Override
    public void create(Book book) {
        group.add(book);
    }

    @Override
    public void update(String ISBN, BookStatus status) {

        Book foundBook = group.stream()
                .filter(book -> book.ISBN.equals(ISBN))
                .findFirst()
                .orElse(null);

        Objects.requireNonNull(foundBook).changeStatus(status);
    }

    @Override
    public Set<Book> search(String ISBN, Set<BookStatus> statuses) {

        Predicate<Book> criteria = book -> true;

        if (Objects.nonNull(ISBN) && !ISBN.isBlank()) {
            criteria = criteria.and(book -> book.getISBN().equals(ISBN));
        }

        if (Objects.nonNull(statuses)) {
            criteria = criteria.and(book -> statuses.contains(book.getStatus()));
        }

        return group.stream()
                .filter(criteria)
                .collect(Collectors.toSet());
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
