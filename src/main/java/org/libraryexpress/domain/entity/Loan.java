package org.libraryexpress.domain.entity;

import java.util.Date;

public class Loan {

    private final Book book;

    private final Client client;

    private boolean active;

    private final Date acquisitionDate;

    private final Date deliveryDate;

    private Loan(Book book, Client client, boolean active, Date acquisitionDate, Date deliveryDate) {
        this.book = book;
        this.client = client;
        this.active = active;
        this.acquisitionDate = acquisitionDate;
        this.deliveryDate = deliveryDate;
    }

    public Book getBook() {
        return book;
    }

    public Client getClient() {
        return client;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public Date getAcquisitionDate() {
        return acquisitionDate;
    }

    public Date getDeliveryDate() {
        return deliveryDate;
    }

    public static class Builder {

        private Book book;

        private Client client;

        private boolean active;

        private Date acquisitionDate;

        private Date deliveryDate;

        Builder book(Book book) {
            return this;
        }

        Builder client(Client client) {
            return this;
        }

        Builder active(boolean active) {
            return this;
        }

        Builder acquisitionDate(Date acquisitionDate) {
            return this;
        }

        Builder deliveryDate(Date deliveryDate) {
            return this;
        }

        Loan buid() {
            return new Loan(book, client, active, acquisitionDate, deliveryDate);
        }
    }
}
