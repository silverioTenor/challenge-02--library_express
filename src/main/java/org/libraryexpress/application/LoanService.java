package org.libraryexpress.application;

import org.libraryexpress.domain.entity.Book;
import org.libraryexpress.domain.entity.Customer;
import org.libraryexpress.domain.entity.Loan;
import org.libraryexpress.domain.enums.LoanStatus;
import org.libraryexpress.domain.repository.ILoanRepository;
import org.libraryexpress.infrastructure.repository.LoanRepository;

import java.time.LocalDate;
import java.util.Set;

import static org.libraryexpress.infrastructure.config.Constant.LOAN_DURATION_IN_DAYS;

public class LoanService {

    private final ILoanRepository loanRepository;

    public LoanService() {
        this.loanRepository = LoanRepository.DB;
    }

    public Loan subscribe(Customer customer, Book book) {
        LocalDate acquisitionDate = LocalDate.now();
        LocalDate deliveryDate = acquisitionDate.plusDays(LOAN_DURATION_IN_DAYS);

        Loan loan = new Loan.Builder()
                .setClient(customer)
                .setBook(book)
                .setAcquisitionDate(acquisitionDate)
                .setDeliveryDate(deliveryDate)
                .setStatus(LoanStatus.BORROWED)
                .build();

        this.loanRepository.create(loan);

        return loan;
    }

    public Set<Loan> getByStatus(LoanStatus status) {
        return this.loanRepository.getByStatus(status);
    }

    public Set<Loan> getByClientId(String clientId) {
        return this.loanRepository.getByClientId(clientId);
    }

    public Set<Loan> getByIsbn(String isbn) {
        return this.loanRepository.getByBookIsbn(isbn);
    }

    public Set<Loan> getAll() {
        return this.loanRepository.all();
    }
}
