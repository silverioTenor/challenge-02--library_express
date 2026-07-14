package org.libraryexpress.domain.entity;

import org.libraryexpress.domain.enums.BookStatus;
import org.libraryexpress.domain.helper.Generator;

import java.util.Objects;

public class Book implements Comparable<Book> {

    public final String ISBN;

    private final String title;

    private final String author;

    private final int year;

    private BookStatus status;

    private Book(String ISBN, String title, String author, int year, BookStatus status) {
        this.ISBN = ISBN;
        this.title = title;
        this.author = author;
        this.year = year;
        this.status = status;
    }

    public String getISBN() {
        return ISBN;
    }

    public String getTitle() {
        return title;
    }

    public String getAuthor() {
        return author;
    }

    public int getYear() {
        return year;
    }

    public BookStatus getStatus() {
        return status;
    }

    public void changeStatus(BookStatus status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "{\n" +
                " ISBN: " + ISBN + ",\n" +
                " title: " + title + ",\n" +
                " author: " + author + ",\n" +
                " year: " + year + ",\n" +
                "}";
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof Book book)) return false;
        return Objects.equals(ISBN, book.ISBN);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(year);
    }

    @Override
    public int compareTo(Book otherBook) {
        return Objects.compare(this.title, otherBook.getTitle(), String::compareTo);
    }

    public static class Builder {

        private String ISBN;

        private String title;

        private String author;

        private int year;

        private BookStatus status;

        public Builder setISBN(String ISBN) {
            this.ISBN = ISBN;
            return this;
        }

        public Builder setTitle(String title) {
            this.title = title;
            return this;
        }

        public Builder setAuthor(String author) {
            this.author = author;
            return this;
        }

        public Builder setYear(int year) {
            this.year = year;
            return this;
        }

        public Builder setStatus(BookStatus status) {
            this.status = status;
            return this;
        }

        public Book build() {
            return new Book(ISBN, title, author, year, status);
        }
    }
}
