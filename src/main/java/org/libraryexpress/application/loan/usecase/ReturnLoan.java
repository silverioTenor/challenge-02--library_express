package org.libraryexpress.application.loan.usecase;

import org.libraryexpress.application.loan.dto.request.FilterLoansDto;
import org.libraryexpress.application.loan.validator.SearchLoanValidator;
import org.libraryexpress.domain.entity.Loan;
import org.libraryexpress.domain.repository.ILoanRepository;
import org.libraryexpress.infrastructure.exception.NotFoundException;
import org.libraryexpress.infrastructure.exception.RuleViolationException;
import org.libraryexpress.infrastructure.repository.LoanRepository;

import java.time.Clock;
import java.util.Optional;
import java.util.Set;

//public class ReturnLoan {
//
//    private final ILoanRepository loanRepository;
//    private final SearchLoanValidator searchLoanValidator;
//
//    public ReturnLoan() {
//        this.loanRepository = LoanRepository.DB;
//        this.searchLoanValidator = new SearchLoanValidator();
//    }
//
//    public void execute(FilterLoansDto filterDto) throws RuleViolationException, NotFoundException {
//
//        this.searchLoanValidator.validate(filterDto);
//
//        Set<Loan> customerLoans = this.loanRepository.search(
//                filterDto.customerId(),
//                filterDto.ISBN(),
//                Set.of(filterDto.getStatus())
//        );
//
//        Optional<Loan> selectedLoan = customerLoans.stream().findFirst();
//
//        if (selectedLoan.isEmpty()) {
//            throw new NotFoundException("No loan found matching the provided parameters.");
//        }
//
//        if (selectedLoan.get().isOverdue(Clock.systemDefaultZone())) {
//            // TODO - implement logic
//        }
//
//
//    }
//}
