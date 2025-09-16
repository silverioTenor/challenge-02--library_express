package org.libraryexpress.domain.entity;

import org.libraryexpress.domain.enums.LoanStatus;

import java.time.LocalDate;
import java.util.Objects;

public class Loan implements Comparable<Loan> {

    private final Book book;

    private final Client client;

    private LoanStatus status;

    private final LocalDate acquisitionDate;

    private final LocalDate deliveryDate;

    private Loan(Book book, Client client, LoanStatus status, LocalDate acquisitionDate, LocalDate deliveryDate) {
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

    public LocalDate getAcquisitionDate() {
        return acquisitionDate;
    }

    public LocalDate getDeliveryDate() {
        return deliveryDate;
    }

    @Override
    public String toString() {
        return "{\n" +
                "book: " + book + ",\n" +
                " client: " + client + ",\n" +
                " status: " + status + ",\n" +
                " acquisitionDate: " + acquisitionDate.toString() + ",\n" +
                " deliveryDate: " + deliveryDate.toString() + ",\n" +
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
        return Objects.compare(acquisitionDate, o.getAcquisitionDate(), LocalDate::compareTo);
    }

    public static class Builder {

        private Book book;

        private Client client;

        private LoanStatus status;

        private LocalDate acquisitionDate;

        private LocalDate deliveryDate;

        public Builder setBook(Book book) {
            this.book = book;
            return this;
        }

        public Builder setClient(Client client) {
            this.client = client;
            return this;
        }

        public Builder setStatus(LoanStatus status) {
            this.status = status;
            return this;
        }

        public Builder setAcquisitionDate(LocalDate acquisitionDate) {
            this.acquisitionDate = acquisitionDate;
            return this;
        }

        public Builder setDeliveryDate(LocalDate deliveryDate) {
            this.deliveryDate = deliveryDate;
            return this;
        }

        public Loan build() {
            return new Loan(book, client, status, acquisitionDate, deliveryDate);
        }
    }
}
