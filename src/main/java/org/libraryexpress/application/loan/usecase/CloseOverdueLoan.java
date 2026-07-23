package org.libraryexpress.application.loan.usecase;

import org.libraryexpress.domain.entity.Loan;
import org.libraryexpress.domain.enums.LoanStatus;
import org.libraryexpress.domain.repository.ILoanRepository;
import org.libraryexpress.infrastructure.exception.NotFoundException;
import org.libraryexpress.infrastructure.exception.RuleViolationException;
import org.libraryexpress.infrastructure.repository.LoanRepository;

/**
 * TODO - Temporary/palliative use case.
 * Once fine calculation and score adjustment are implemented directly in ReturnLoan,
 * this use case should become obsolete — OVERDUE loans should transition to FINISHED
 * automatically at return time, not via manual intervention.
 */
public class CloseOverdueLoan {

    private final ILoanRepository loanRepository;

    public CloseOverdueLoan() {
        this.loanRepository = LoanRepository.DB;
    }

    public void execute(String loanId) throws NotFoundException, RuleViolationException {

        Loan loan = this.loanRepository.findById(loanId)
                .orElseThrow(() -> new NotFoundException("Loan not found!"));

        if (!loan.getStatus().equals(LoanStatus.OVERDUE)) {
            throw new RuleViolationException("Only overdue loans can be closed through this flow.");
        }

        loan.changeStatus(LoanStatus.FINISHED);
        this.loanRepository.update(loan);
    }
}
