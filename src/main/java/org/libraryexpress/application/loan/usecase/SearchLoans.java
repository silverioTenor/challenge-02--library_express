package org.libraryexpress.application.loan.usecase;

import org.libraryexpress.application.loan.dto.request.FilterLoansDto;
import org.libraryexpress.domain.entity.Loan;
import org.libraryexpress.domain.repository.ILoanRepository;
import org.libraryexpress.infrastructure.repository.LoanRepository;

import java.util.Set;

public class SearchLoans {

    private final ILoanRepository loanRepository;

    public SearchLoans() {
        this.loanRepository = LoanRepository.DB;
    }

    public Set<Loan> execute(FilterLoansDto filter) {

        boolean hasAnyCriteria = filter.status() != null
                && (filter.customerId() != null && !filter.customerId().isBlank())
                && (filter.ISBN() != null && !filter.ISBN().isBlank());

        if (!hasAnyCriteria) throw new IllegalArgumentException("At least one search criteria must be provided");

        return loanRepository.search(filter.customerId(), filter.ISBN(), Set.of(filter.status()));
    }
}
