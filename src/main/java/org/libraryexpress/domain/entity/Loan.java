package org.libraryexpress.domain.entity;

import org.libraryexpress.domain.enums.LoanStatus;

import java.time.LocalDate;
import java.util.Objects;

public class Loan implements Comparable<Loan> {

    private final String id;

    private final String ISBN;

    private final String customerId;

    private LoanStatus status;

    private final LocalDate acquisitionDate;

    private final LocalDate deliveryDate;

    private Loan(
            String id,
            String ISBN,
            String customerId,
            LoanStatus status,
            LocalDate acquisitionDate,
            LocalDate deliveryDate
    ) {
        this.id = id;
        this.ISBN = ISBN;
        this.customerId = customerId;
        this.status = status;
        this.acquisitionDate = acquisitionDate;
        this.deliveryDate = deliveryDate;
    }

    public String getId() {
        return id;
    }

    public String getISBN() {
        return ISBN;
    }

    public String getCustomerId() {
        return customerId;
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
                " ID: " + id + ",\n" +
                " ISBN: " + ISBN + ",\n" +
                " customerId: " + customerId + ",\n" +
                " status: " + status + ",\n" +
                " acquisitionDate: " + acquisitionDate.toString() + ",\n" +
                " deliveryDate: " + deliveryDate.toString() + ",\n" +
                "}";
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof Loan loan)) return false;
        return Objects.equals(ISBN, loan.ISBN) && Objects.equals(acquisitionDate, loan.acquisitionDate);
    }

    @Override
    public int hashCode() {
        return Objects.hash(ISBN, acquisitionDate);
    }

    @Override
    public int compareTo(Loan o) {
        return Objects.compare(acquisitionDate, o.getAcquisitionDate(), LocalDate::compareTo);
    }

    public static class Builder {

        private String id;

        private String ISBN;

        private String customerId;

        private LoanStatus status;

        private LocalDate acquisitionDate;

        private LocalDate deliveryDate;

        public Builder setId(String id) {
            this.id = id;
            return this;
        }

        public Builder setISBN(String ISBN) {
            this.ISBN = ISBN;
            return this;
        }

        public Builder setCustomerId(String customerId) {
            this.customerId = customerId;
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
            return new Loan(id, ISBN, customerId, status, acquisitionDate, deliveryDate);
        }
    }
}
