package org.libraryexpress.domain.repository;

import org.libraryexpress.domain.entity.Loan;
import org.libraryexpress.domain.enums.LoanStatus;

import java.util.Optional;
import java.util.Set;

public interface LoanRepository {
    void create(Loan loan);
    void update(Loan loanToUpdate);
    Optional<Loan> findById(String loanId);
    Set<Loan> search(String customerId, String ISBN, Set<LoanStatus> statuses);
    Set<Loan> all();
}
