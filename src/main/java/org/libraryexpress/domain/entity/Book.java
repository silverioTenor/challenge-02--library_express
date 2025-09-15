package org.libraryexpress.domain.entity;

import org.libraryexpress.helper.Generator;

public class Book {

    public final String ISBN = Generator.genISBN();

    private final String title;

    private final String author;

    private final String year;

    private String qty;

    private Book(String title, String author, String year, String qty) {
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

    public String getYear() {
        return year;
    }

    public String getQty() {
        return qty;
    }

    public void changeQty(String qty) {
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

    public static class Builder {

        private String title;

        private String author;

        private String year;

        private String qty;

        Builder title(String title) {
            return this;
        }

        Builder author(String author) {
            return this;
        }

        Builder year(String year) {
            return this;
        }

        Builder qty(String qty) {
            return this;
        }

        Book build() {
            return new Book(title, author, year, qty);
        }
    }
}
