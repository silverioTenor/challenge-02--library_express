package org.libraryexpress.domain.loan.validator;

import org.libraryexpress.domain.loan.exception.LoanLimitReachedException;
import org.libraryexpress.domain.loan.exception.OverdueLoanException;
import org.libraryexpress.domain.loan.entity.Loan;
import org.libraryexpress.domain.loan.enums.LoanStatus;
import org.libraryexpress.domain.loan.repository.LoanRepository;

import java.util.Set;

public class LoanEligibilityValidator {

    private final LoanRepository loanRepository;

    public LoanEligibilityValidator(LoanRepository loanRepository) {
        this.loanRepository = loanRepository;
    }

    public void validate(String customerId) throws OverdueLoanException, LoanLimitReachedException {

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
