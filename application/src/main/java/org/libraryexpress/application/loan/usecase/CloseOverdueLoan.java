package org.libraryexpress.application.loan.usecase;

import org.libraryexpress.domain.loan.exception.LoanNotFoundException;
import org.libraryexpress.domain.loan.exception.OverdueLoanException;
import org.libraryexpress.domain.loan.entity.Loan;
import org.libraryexpress.domain.loan.enums.LoanStatus;
import org.libraryexpress.domain.loan.repository.LoanRepository;

/**
 * TODO - Temporary/palliative use case.
 * Once fine calculation and score adjustment are implemented directly in ReturnLoan,
 * this use case should become obsolete — OVERDUE loans should transition to FINISHED
 * automatically at return time, not via manual intervention.
 */
public class CloseOverdueLoan {

    private final LoanRepository loanRepository;

    public CloseOverdueLoan(LoanRepository loanRepository) {
        this.loanRepository = loanRepository;
    }

    public void execute(String loanId) throws LoanNotFoundException, OverdueLoanException {

        Loan loan = this.loanRepository.findById(loanId)
                .orElseThrow(LoanNotFoundException::new);

        if (!loan.getStatus().equals(LoanStatus.OVERDUE)) {
            throw new OverdueLoanException();
        }

        loan.changeStatus(LoanStatus.FINISHED);
        this.loanRepository.update(loan);
    }
}
