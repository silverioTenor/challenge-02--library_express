package org.libraryexpress.application.loan.usecase;

import org.libraryexpress.domain.book.entity.Book;
import org.libraryexpress.domain.book.exception.BookNotFoundException;
import org.libraryexpress.domain.loan.exception.InvalidLoanStatusException;
import org.libraryexpress.domain.loan.exception.LoanNotFoundException;
import org.libraryexpress.domain.loan.entity.Loan;
import org.libraryexpress.domain.book.enums.BookStatus;
import org.libraryexpress.domain.loan.enums.LoanStatus;
import org.libraryexpress.domain.book.repository.BookRepository;
import org.libraryexpress.domain.loan.repository.LoanRepository;

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

    public void execute(String loanId) {

        Loan loan = this.loanRepository.findById(loanId)
                .orElseThrow(() -> new LoanNotFoundException("No loan found matching the provided parameters."));

        if (!ALLOWED_STATUSES.contains(loan.getStatus())) {
            throw new InvalidLoanStatusException("The Loan cannot be completed with the current status.");
        }

        Book book = this.bookRepository.getByIsbn(loan.getISBN().value())
                .orElseThrow(BookNotFoundException::new);

        // TODO - v2 - use JOB to change LOAN status and then send e-mail/notification
        LoanStatus updatedStatus = loan.isOverdue(Clock.systemDefaultZone())
                ? LoanStatus.OVERDUE
                : LoanStatus.FINISHED;

        loan.changeStatus(updatedStatus);
        book.changeStatus(BookStatus.AVAILABLE);

        this.loanRepository.update(loan);
        this.bookRepository.update(book);
    }
}
