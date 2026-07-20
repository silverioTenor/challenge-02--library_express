package org.libraryexpress.application.loan.usecase;

import org.libraryexpress.application.loan.dto.request.FilterLoansDto;
import org.libraryexpress.application.loan.dto.response.LoanDto;
import org.libraryexpress.application.loan.mapper.LoanMapper;
import org.libraryexpress.domain.entity.Loan;
import org.libraryexpress.domain.repository.ILoanRepository;
import org.libraryexpress.infrastructure.exception.RuleViolationException;
import org.libraryexpress.infrastructure.repository.LoanRepository;

import java.util.Set;
import java.util.stream.Collectors;

public class SearchLoans {

    private final ILoanRepository loanRepository;
    private final LoanMapper mapper;

    public SearchLoans() {
        this.loanRepository = LoanRepository.DB;
        this.mapper = LoanMapper.INSTANCE;
    }

    public Set<LoanDto> execute(FilterLoansDto filter) throws RuleViolationException {

        boolean hasAnyCriteria = filter.status() != null
                || (filter.customerId() != null && !filter.customerId().isBlank())
                || (filter.ISBN() != null && !filter.ISBN().isBlank());

        if (!hasAnyCriteria) throw new RuleViolationException("At least one search criteria must be provided");

        Set<Loan> loans = this.loanRepository.search(filter.customerId(), filter.ISBN(), Set.of(filter.status()));

        return loans.stream()
                .map(mapper::toResponseDto)
                .collect(Collectors.toSet());
    }
}
