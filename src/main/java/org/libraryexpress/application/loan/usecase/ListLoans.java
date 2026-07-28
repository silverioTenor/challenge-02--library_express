package org.libraryexpress.application.loan.usecase;

import org.libraryexpress.application.loan.dto.response.LoanDto;
import org.libraryexpress.application.loan.mapper.LoanMapper;
import org.libraryexpress.domain.repository.LoanRepository;

import java.util.Set;
import java.util.stream.Collectors;

public class ListLoans {

    private final LoanRepository loanRepository;
    private final LoanMapper mapper;

    public ListLoans(LoanRepository loanRepository, LoanMapper mapper) {
        this.loanRepository = loanRepository;
        this.mapper = mapper;
    }

    public Set<LoanDto> execute() {

        return this.loanRepository.all()
                .stream()
                .map(mapper::toResponseDto)
                .collect(Collectors.toSet());
    }
}
