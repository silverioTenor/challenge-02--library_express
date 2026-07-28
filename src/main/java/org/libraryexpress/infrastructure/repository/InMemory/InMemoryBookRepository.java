package org.libraryexpress.infrastructure.repository.InMemory;

import org.libraryexpress.domain.entity.Book;
import org.libraryexpress.domain.enums.BookStatus;
import org.libraryexpress.domain.repository.BookRepository;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class InMemoryBookRepository implements BookRepository {

    private final Map<String, Book> group = new ConcurrentHashMap<>();

    @Override
    public void create(Book book) {
        group.put(book.getISBN(), book);
    }

    @Override
    public void update(String ISBN, BookStatus status) {
        Optional.ofNullable(group.get(ISBN))
                .ifPresent(book -> book.changeStatus(status));
    }

    @Override
    public Set<Book> search(String ISBN, Set<BookStatus> statuses) {

        Collection<Book> books = (Objects.nonNull(ISBN) && !ISBN.isBlank())
                ? Optional.ofNullable(group.get(ISBN)).map(Set::of).orElse(Set.of())
                : group.values();

        if (Objects.nonNull(statuses) && !statuses.isEmpty()) {
            return books.stream()
                    .filter(book -> statuses.contains(book.getStatus()))
                    .collect(Collectors.toUnmodifiableSet());
        }

        return Set.copyOf(books);
    }

    @Override
    public Optional<Book> getByIsbn(String isbn) {
        return Optional.ofNullable(group.get(isbn));
    }

    @Override
    public Set<Book> all() {
        return Set.copyOf(group.values());
    }
}
