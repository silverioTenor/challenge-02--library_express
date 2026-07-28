package org.libraryexpress.domain.entity;

import org.libraryexpress.domain.enums.LoanStatus;

import java.time.Clock;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.Objects;

public class Loan implements Comparable<Loan> {

    public static final int MAX_ACTIVE_LOANS = 2;

    private static final int MAX_DAY_TO_RETURN = 15;

    private final String id;

    private final String ISBN;

    private final String customerId;

    private LoanStatus status;

    private final LocalDate startDate;

    private final LocalDate endDate;

    private Loan(
            String id,
            String ISBN,
            String customerId,
            LoanStatus status,
            LocalDate startDate,
            LocalDate endDate
    ) {
        this.id = id;
        this.ISBN = ISBN;
        this.customerId = customerId;
        this.status = status;
        this.startDate = startDate;
        this.endDate = endDate != null ? endDate : this.dueDate();
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

    public LoanStatus getStatus() {
        return status;
    }

    public void changeStatus(LoanStatus status) {
        this.status = status;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public boolean isOverdue(Clock clock) {
        return ChronoUnit.DAYS.between(startDate, LocalDate.now(clock)) > MAX_DAY_TO_RETURN;
    }

    private LocalDate dueDate() {
        return this.startDate.plusDays(MAX_DAY_TO_RETURN);
    }

    @Override
    public String toString() {
        return "{\n" +
                " id: " + id + ",\n" +
                " ISBN: " + ISBN + ",\n" +
                " customerId: " + customerId + ",\n" +
                " statuses: " + status.toString() + ",\n" +
                " startDate: " + startDate + ",\n" +
                " endDate: " + endDate + ",\n" +
                "}";
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof Loan loan)) return false;
        return Objects.equals(id, loan.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public int compareTo(Loan o) {
        return Objects.compare(startDate, o.getStartDate(), LocalDate::compareTo);
    }

    public static class Builder {

        private String id;

        private String ISBN;

        private String customerId;

        private LoanStatus status;

        private LocalDate startDate;

        private LocalDate endDate;

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

        public Builder setStartDate(LocalDate startDate) {
            this.startDate = startDate;
            return this;
        }

        public Builder setEndDate(LocalDate endDate) {
            this.endDate = endDate;
            return this;
        }

        public Loan build() {
            return new Loan(id, ISBN, customerId, status, startDate, endDate);
        }
    }
}
