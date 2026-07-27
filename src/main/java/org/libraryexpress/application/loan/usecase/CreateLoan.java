package org.libraryexpress.application.loan.usecase;

import org.libraryexpress.application.book.helper.BookAvailability;
import org.libraryexpress.application.loan.dto.request.CreateLoanDto;
import org.libraryexpress.application.loan.helper.LoanEligibility;
import org.libraryexpress.domain.entity.Loan;
import org.libraryexpress.domain.enums.BookStatus;
import org.libraryexpress.domain.enums.LoanStatus;
import org.libraryexpress.domain.helper.Generator;
import org.libraryexpress.domain.repository.IBookRepository;
import org.libraryexpress.domain.repository.ILoanRepository;
import org.libraryexpress.infrastructure.exception.NotFoundException;
import org.libraryexpress.infrastructure.exception.RuleViolationException;
import org.libraryexpress.infrastructure.repository.BookRepository;
import org.libraryexpress.infrastructure.repository.LoanRepository;

import java.time.LocalDate;

public class CreateLoan {

    private final ILoanRepository loanRepository;
    private final IBookRepository bookRepository;
    private final LoanEligibility loanEligibility;
    private final BookAvailability bookAvailability;

    public CreateLoan(
            ILoanRepository loanRepository,
            IBookRepository bookRepository,
            LoanEligibility loanEligibility,
            BookAvailability bookAvailability
    ) {
        this.loanRepository = loanRepository;
        this.bookRepository = bookRepository;
        this.loanEligibility = loanEligibility;
        this.bookAvailability = bookAvailability;
    }

    public void execute(CreateLoanDto createLoanDto) throws RuleViolationException, NotFoundException {

        this.loanEligibility.check(createLoanDto.customerId());
        this.bookAvailability.check(createLoanDto.ISBN());

        String id = Generator.genUUID();

        Loan loan = new Loan.Builder()
                .setId(id)
                .setISBN(createLoanDto.ISBN())
                .setCustomerId(createLoanDto.customerId())
                .setStatus(LoanStatus.ACTIVE)
                .setStartDate(LocalDate.now())
                .build();

        this.bookRepository.update(createLoanDto.ISBN(), BookStatus.BORROWED);

        this.loanRepository.create(loan);
    }
}
