package org.libraryexpress.application.loan.validator;

import org.libraryexpress.domain.entity.Loan;
import org.libraryexpress.domain.enums.LoanStatus;
import org.libraryexpress.domain.repository.ILoanRepository;
import org.libraryexpress.domain.validator.IValidator;
import org.libraryexpress.infrastructure.exception.RuleViolationException;
import org.libraryexpress.infrastructure.repository.LoanRepository;

import java.util.Set;

public class LoanEligibilityValidator implements IValidator<String> {

    private final ILoanRepository loanRepository;

    public LoanEligibilityValidator() {
        this.loanRepository = LoanRepository.DB;
    }

    @Override
    public void validate(String customerId) throws RuleViolationException {

        Set<Loan> loans = this.loanRepository.findBy(
                customerId,
                null,
                Set.of(LoanStatus.ACTIVE, LoanStatus.OVERDUE)
        );

        boolean hasOverdue = loans.stream()
                .anyMatch(loan -> loan.getStatus() == LoanStatus.OVERDUE);

        if (hasOverdue) {
            throw new RuleViolationException("Customer has a pending overdue return.");
        }

        long activeCount = loans.stream()
                .filter(loan -> loan.getStatus() == LoanStatus.ACTIVE)
                .count();

        if (activeCount > 1) {
            throw new RuleViolationException("Customer already has more than one active loan.");
        }
    }
}
