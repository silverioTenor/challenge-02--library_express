package org.libraryexpress.application.loan.usecase;

import org.libraryexpress.application.book.validator.BookAvailabilityValidator;
import org.libraryexpress.application.loan.dto.request.CreateLoanDto;
import org.libraryexpress.application.loan.validator.LoanEligibilityValidator;
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

import java.time.Clock;
import java.time.LocalDate;

public class CreateLoan {

    private final ILoanRepository loanRepository;
    private final IBookRepository bookRepository;
    private final LoanEligibilityValidator loanEligibilityValidator;
    private final BookAvailabilityValidator bookAvailabilityValidator;

    public CreateLoan() {
        this.loanRepository = LoanRepository.DB;
        this.bookRepository = BookRepository.DB;
        this.loanEligibilityValidator = new LoanEligibilityValidator();
        this.bookAvailabilityValidator = new BookAvailabilityValidator();
    }

    public void execute(CreateLoanDto createLoanDto) throws RuleViolationException, NotFoundException {

        this.loanEligibilityValidator.validate(createLoanDto.customerId());
        this.bookAvailabilityValidator.validate(createLoanDto.ISBN());

        String id = Generator.genUUID();

        Loan loan = new Loan.Builder()
                .setId(id)
                .setISBN(createLoanDto.ISBN())
                .setCustomerId(createLoanDto.customerId())
                .setStatus(LoanStatus.ACTIVE)
                .setStartDate(LocalDate.now())
                .setEndDate(LocalDate.now(Clock.systemDefaultZone()).plusDays(15))
                .build();

        this.bookRepository.update(createLoanDto.ISBN(), BookStatus.BORROWED);

        this.loanRepository.create(loan);
    }
}
