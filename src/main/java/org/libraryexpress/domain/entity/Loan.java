package org.libraryexpress.domain.entity;

import org.libraryexpress.enums.LoanStatus;

import java.util.Date;
import java.util.Objects;

public class Loan implements Comparable<Loan> {

    private final Book book;

    private final Client client;

    private LoanStatus status;

    private final Date acquisitionDate;

    private final Date deliveryDate;

    private Loan(Book book, Client client, LoanStatus status, Date acquisitionDate, Date deliveryDate) {
        this.book = book;
        this.client = client;
        this.status = status;
        this.acquisitionDate = acquisitionDate;
        this.deliveryDate = deliveryDate;
    }

    public Book getBook() {
        return book;
    }

    public Client getClient() {
        return client;
    }

    public LoanStatus status() {
        return status;
    }

    public void changeStatus(LoanStatus status) {
        this.status = status;
    }

    public Date getAcquisitionDate() {
        return acquisitionDate;
    }

    public Date getDeliveryDate() {
        return deliveryDate;
    }

    @Override
    public String toString() {
        return "{\n" +
                "book: " + book + ",\n" +
                " client: " + client + ",\n" +
                " status: " + status + ",\n" +
                " acquisitionDate: " + acquisitionDate + ",\n" +
                " deliveryDate: " + deliveryDate + ",\n" +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof Loan loan)) return false;
        return Objects.equals(book, loan.book) && Objects.equals(acquisitionDate, loan.acquisitionDate);
    }

    @Override
    public int hashCode() {
        return Objects.hash(book, acquisitionDate);
    }

    @Override
    public int compareTo(Loan o) {
        return Objects.compare(acquisitionDate, o.getAcquisitionDate(), Date::compareTo);
    }

    public static class Builder {

        private Book book;

        private Client client;

        private LoanStatus status;

        private Date acquisitionDate;

        private Date deliveryDate;

        Builder book(Book book) {
            return this;
        }

        Builder client(Client client) {
            return this;
        }

        Builder status(LoanStatus status) {
            return this;
        }

        Builder acquisitionDate(Date acquisitionDate) {
            return this;
        }

        Builder deliveryDate(Date deliveryDate) {
            return this;
        }

        Loan buid() {
            return new Loan(book, client, status, acquisitionDate, deliveryDate);
        }
    }
}
