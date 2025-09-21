package org.libraryexpress.domain.entity;

import org.libraryexpress.domain.helper.Generator;

public class WaitingList {

    private final String ID;

    private final String isbn;

    private final String clientEmail;

    public WaitingList(String isbn, String clientEmail) {
        this.ID = Generator.genUUID();
        this.isbn = isbn;
        this.clientEmail = clientEmail;
    }

    public String getID() {
        return ID;
    }

    public String getISBN() {
        return isbn;
    }

    public String getClientEmail() {
        return clientEmail;
    }

    public static class Builder {

        private String isbn;

        private String clientEmail;

        public Builder() {}

        public Builder setISBN(String isbn) {
            this.isbn = isbn;
            return this;
        }

        public Builder setClientEmail(String clientEmail) {
            this.clientEmail = clientEmail;
            return this;
        }

        public WaitingList build() {
            return new WaitingList(isbn, clientEmail);
        }
    }
}
