package org.libraryexpress.domain.repository;

import org.libraryexpress.domain.entity.Loan;
import org.libraryexpress.enums.LoanStatus;

import java.util.Optional;
import java.util.Set;

public interface ILoanRepository {

    void create(Loan loan);
    boolean update(Loan loanToUpdate);

    Optional<Loan> getByStatus(LoanStatus status);
    Set<Loan> getByClientId(String clientId);
    Set<Loan> getByBookIsbn(String isbn);
    Set<Loan> all();
}
