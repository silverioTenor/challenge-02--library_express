package org.libraryexpress.domain.repository;

import org.libraryexpress.domain.entity.Loan;
import org.libraryexpress.domain.enums.LoanStatus;

import java.util.Set;

public interface ILoanRepository {

    void create(Loan loan);
    boolean update(Loan loanToUpdate);
    Set<Loan> search(String customerId, String ISBN, Set<LoanStatus> statuses);
    Set<Loan> all();
}
