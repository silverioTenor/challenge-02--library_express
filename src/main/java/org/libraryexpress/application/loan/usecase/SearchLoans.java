package org.libraryexpress.application.loan.usecase;

import org.libraryexpress.application.loan.dto.request.FilterLoansDto;
import org.libraryexpress.application.loan.dto.response.LoanDto;
import org.libraryexpress.application.loan.mapper.LoanMapper;
import org.libraryexpress.application.loan.validator.SearchLoanValidator;
import org.libraryexpress.domain.entity.Loan;
import org.libraryexpress.domain.repository.ILoanRepository;
import org.libraryexpress.infrastructure.exception.RuleViolationException;
import org.libraryexpress.infrastructure.repository.LoanRepository;

import java.util.Set;
import java.util.stream.Collectors;

public class SearchLoans {

    private final ILoanRepository loanRepository;
    private final LoanMapper mapper;
    private final SearchLoanValidator searchLoanValidator;

    public SearchLoans(ILoanRepository loanRepository, LoanMapper mapper, SearchLoanValidator searchLoanValidator) {
        this.loanRepository = loanRepository;
        this.mapper = mapper;
        this.searchLoanValidator = searchLoanValidator;
    }

    public Set<LoanDto> execute(FilterLoansDto filter) throws RuleViolationException {

        this.searchLoanValidator.validate(filter);

        Set<Loan> loans = this.loanRepository.find(filter.customerId(), filter.ISBN(), filter.statuses());

        return loans.stream()
                .map(mapper::toResponseDto)
                .collect(Collectors.toSet());
    }
}
