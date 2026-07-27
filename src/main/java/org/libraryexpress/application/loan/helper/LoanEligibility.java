package org.libraryexpress.application.loan.helper;

import org.libraryexpress.domain.entity.Loan;
import org.libraryexpress.domain.enums.LoanStatus;
import org.libraryexpress.domain.repository.ILoanRepository;
import org.libraryexpress.infrastructure.exception.RuleViolationException;
import org.libraryexpress.infrastructure.repository.LoanRepository;

import java.util.Set;

public class LoanEligibility {

    private final ILoanRepository loanRepository;

    public LoanEligibility(ILoanRepository loanRepository) {
        this.loanRepository = loanRepository;
    }

    public void check(String customerId) throws RuleViolationException {

        Set<Loan> loans = this.loanRepository.find(
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

        if (activeCount >= Loan.MAX_ACTIVE_LOANS) {
            throw new RuleViolationException("Customer has reached the maximum limit of active loans.");
        }
    }
}
