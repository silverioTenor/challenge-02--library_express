package org.libraryexpress.application.loan.usecase;

import org.libraryexpress.application.loan.exception.InvalidLoanStatusException;
import org.libraryexpress.application.loan.exception.LoanNotFoundException;
import org.libraryexpress.domain.entity.Loan;
import org.libraryexpress.domain.enums.BookStatus;
import org.libraryexpress.domain.enums.LoanStatus;
import org.libraryexpress.domain.repository.BookRepository;
import org.libraryexpress.domain.repository.LoanRepository;

import java.time.Clock;
import java.util.Set;

public class ReturnLoan {

    private static final Set<LoanStatus> ALLOWED_STATUSES = Set.of(LoanStatus.ACTIVE, LoanStatus.OVERDUE);

    private final LoanRepository loanRepository;
    private final BookRepository bookRepository;

    public ReturnLoan(LoanRepository loanRepository, BookRepository bookRepository) {
        this.loanRepository = loanRepository;
        this.bookRepository = bookRepository;
    }

    public void execute(String loanId) throws LoanNotFoundException, InvalidLoanStatusException {

        Loan loan = this.loanRepository.findById(loanId)
                .orElseThrow(() -> new LoanNotFoundException("No loan found matching the provided parameters."));

        if (!ALLOWED_STATUSES.contains(loan.getStatus())) {
            throw new InvalidLoanStatusException("The Loan cannot be completed with the current status.");
        }

        // TODO - v2 - use JOB to change LOAN status and then send e-mail/notification
        LoanStatus updatedStatus = loan.isOverdue(Clock.systemDefaultZone())
                ? LoanStatus.OVERDUE
                : LoanStatus.FINISHED;

        loan.changeStatus(updatedStatus);
        this.loanRepository.update(loan);

        this.bookRepository.update(loan.getISBN(), BookStatus.AVAILABLE);
    }
}
