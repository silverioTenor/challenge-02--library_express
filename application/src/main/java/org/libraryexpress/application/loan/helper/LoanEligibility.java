package org.libraryexpress.application.loan.helper;

import org.libraryexpress.application.loan.exception.LoanLimitReachedException;
import org.libraryexpress.application.loan.exception.OverdueLoanException;
import org.libraryexpress.domain.entity.Loan;
import org.libraryexpress.domain.enums.LoanStatus;
import org.libraryexpress.domain.repository.LoanRepository;
import org.libraryexpress.domain.exception.RuleViolationException;

import java.util.Set;

public class LoanEligibility {

    private final LoanRepository loanRepository;

    public LoanEligibility(LoanRepository loanRepository) {
        this.loanRepository = loanRepository;
    }

    public void check(String customerId) throws OverdueLoanException, LoanLimitReachedException {

        Set<Loan> loans = this.loanRepository.search(
                customerId,
                null,
                Set.of(LoanStatus.ACTIVE, LoanStatus.OVERDUE)
        );

        boolean hasOverdue = loans.stream()
                .anyMatch(loan -> loan.getStatus() == LoanStatus.OVERDUE);

        if (hasOverdue) {
            throw new OverdueLoanException("Customer has a pending overdue return.");
        }

        long activeCount = loans.stream()
                .filter(loan -> loan.getStatus() == LoanStatus.ACTIVE)
                .count();

        if (activeCount >= Loan.MAX_ACTIVE_LOANS) {
            throw new LoanLimitReachedException("Customer has reached the maximum limit of active loans.");
        }
    }
}
