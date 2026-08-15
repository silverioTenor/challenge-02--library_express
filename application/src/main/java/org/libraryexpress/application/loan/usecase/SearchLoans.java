package org.libraryexpress.application.loan.usecase;

import org.libraryexpress.application.loan.dto.request.FilterLoansDto;
import org.libraryexpress.application.loan.dto.response.LoanDto;
import org.libraryexpress.application.loan.mapper.LoanMapper;
import org.libraryexpress.application.loan.validator.SearchLoanValidator;
import org.libraryexpress.domain.loan.entity.Loan;
import org.libraryexpress.domain.loan.repository.LoanRepository;

import java.util.Set;
import java.util.stream.Collectors;

public class SearchLoans {

    private final LoanRepository loanRepository;
    private final LoanMapper mapper;
    private final SearchLoanValidator searchLoanValidator;

    public SearchLoans(LoanRepository loanRepository, LoanMapper mapper, SearchLoanValidator searchLoanValidator) {
        this.loanRepository = loanRepository;
        this.mapper = mapper;
        this.searchLoanValidator = searchLoanValidator;
    }

    public Set<LoanDto> execute(FilterLoansDto filter) {

        this.searchLoanValidator.validate(filter);

        Set<Loan> loans = this.loanRepository.search(filter.customerId(), filter.ISBN(), filter.statuses());

        return loans.stream()
                .map(mapper::toResponseDto)
                .collect(Collectors.toSet());
    }
}
