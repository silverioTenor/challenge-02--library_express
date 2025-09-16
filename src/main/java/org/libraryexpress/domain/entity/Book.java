package org.libraryexpress.domain.entity;

import org.libraryexpress.domain.helper.Generator;

import java.util.Objects;

public class Book implements Comparable<Book> {

    public final String ISBN = Generator.genISBN();

    private final String title;

    private final String author;

    private final int year;

    private int qty;

    private Book(String title, String author, int year, int qty) {
        this.title = title;
        this.author = author;
        this.year = year;
        this.qty = qty;
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

    public int getQty() {
        return qty;
    }

    public void changeQty(int qty) {
        this.qty = qty;
    }

    @Override
    public String toString() {
        return "{\n" +
                " isbn: " + ISBN + ",\n" +
                " title: " + title + ",\n" +
                " author: " + author + ",\n" +
                " year: " + year + ",\n" +
                " qty: " + qty + ",\n" +
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

        private String title;

        private String author;

        private int year;

        private int qty;

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

        public Builder setQty(int qty) {
            this.qty = qty;
            return this;
        }

        public Book build() {
            return new Book(title, author, year, qty);
        }
    }
}
