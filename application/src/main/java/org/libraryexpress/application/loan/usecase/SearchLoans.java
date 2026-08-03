package org.libraryexpress.application.loan.usecase;

import org.libraryexpress.application.loan.dto.request.FilterLoansDto;
import org.libraryexpress.application.loan.dto.response.LoanDto;
import org.libraryexpress.application.loan.exception.SearchLoanValidationException;
import org.libraryexpress.application.loan.mapper.LoanMapper;
import org.libraryexpress.application.loan.validator.SearchLoanValidation;
import org.libraryexpress.domain.entity.Loan;
import org.libraryexpress.domain.repository.LoanRepository;

import java.util.Set;
import java.util.stream.Collectors;

public class SearchLoans {

    private final LoanRepository loanRepository;
    private final LoanMapper mapper;
    private final SearchLoanValidation searchLoanValidation;

    public SearchLoans(LoanRepository loanRepository, LoanMapper mapper, SearchLoanValidation searchLoanValidation) {
        this.loanRepository = loanRepository;
        this.mapper = mapper;
        this.searchLoanValidation = searchLoanValidation;
    }

    public Set<LoanDto> execute(FilterLoansDto filter) throws SearchLoanValidationException {

        this.searchLoanValidation.validate(filter);

        Set<Loan> loans = this.loanRepository.search(filter.customerId(), filter.ISBN(), filter.statuses());

        return loans.stream()
                .map(mapper::toResponseDto)
                .collect(Collectors.toSet());
    }
}
