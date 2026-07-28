package org.libraryexpress.infrastructure.config;

import org.libraryexpress.application.book.helper.BookAvailability;
import org.libraryexpress.application.book.mapper.BookMapper;
import org.libraryexpress.application.book.usecase.FindBook;
import org.libraryexpress.application.book.usecase.ListBooks;
import org.libraryexpress.application.book.usecase.RegisterBook;
import org.libraryexpress.application.customer.mapper.CustomerMapper;
import org.libraryexpress.application.customer.usecase.CreateCustomer;
import org.libraryexpress.application.customer.usecase.FindCustomer;
import org.libraryexpress.application.customer.usecase.ListCustomers;
import org.libraryexpress.application.customer.usecase.UpdateCustomerEmail;
import org.libraryexpress.application.loan.helper.LoanEligibility;
import org.libraryexpress.application.loan.mapper.LoanMapper;
import org.libraryexpress.application.loan.usecase.*;
import org.libraryexpress.application.loan.validator.SearchLoanValidator;
import org.libraryexpress.infrastructure.repository.InMemory.InMemoryBookRepository;
import org.libraryexpress.infrastructure.repository.InMemory.InMemoryCustomerRepository;
import org.libraryexpress.infrastructure.repository.InMemory.InMemoryLoanRepository;

public class AppContext {

    // CUSTOMER
    private final CreateCustomer createCustomer;
    private final FindCustomer findCustomer;
    private final ListCustomers listCustomers;
    private final UpdateCustomerEmail updateCustomerEmail;

    // BOOK
    private final RegisterBook registerBook;
    private final FindBook findBook;
    private final ListBooks listBooks;

    // LOAN
    private final CreateLoan createLoan;
    private final SearchLoans searchLoans;
    private final ListLoans listLoans;
    private final ReturnLoan returnLoan;
    private final CloseOverdueLoan closeOverdueLoan;

    public AppContext() {

        // CUSTOMER
        InMemoryCustomerRepository inMemoryCustomerRepository = new InMemoryCustomerRepository();

        this.createCustomer = new CreateCustomer(inMemoryCustomerRepository, CustomerMapper.INSTANCE);
        this.findCustomer = new FindCustomer(inMemoryCustomerRepository, CustomerMapper.INSTANCE);
        this.listCustomers = new ListCustomers(inMemoryCustomerRepository, CustomerMapper.INSTANCE);
        this.updateCustomerEmail = new UpdateCustomerEmail(inMemoryCustomerRepository, CustomerMapper.INSTANCE);

        // BOOK
        InMemoryBookRepository inMemoryBookRepository = new InMemoryBookRepository();
        BookAvailability bookAvailability = new BookAvailability(inMemoryBookRepository);

        this.registerBook = new RegisterBook(inMemoryBookRepository, BookMapper.INSTANCE);
        this.findBook = new FindBook(inMemoryBookRepository, BookMapper.INSTANCE);
        this.listBooks = new ListBooks(inMemoryBookRepository, BookMapper.INSTANCE);

        // LOAN
        InMemoryLoanRepository inMemoryLoanRepository = new InMemoryLoanRepository();
        LoanEligibility loanEligibility = new LoanEligibility(inMemoryLoanRepository);
        SearchLoanValidator searchLoanValidator = new SearchLoanValidator();

        this.createLoan = new CreateLoan(inMemoryLoanRepository, inMemoryBookRepository, loanEligibility, bookAvailability);
        this.searchLoans = new SearchLoans(inMemoryLoanRepository, LoanMapper.INSTANCE, searchLoanValidator);
        this.listLoans = new ListLoans(inMemoryLoanRepository, LoanMapper.INSTANCE);
        this.returnLoan = new ReturnLoan(inMemoryLoanRepository, inMemoryBookRepository);
        this.closeOverdueLoan = new CloseOverdueLoan(inMemoryLoanRepository);
    }

    public CreateCustomer getCreateCustomer() {
        return createCustomer;
    }

    public FindCustomer getFindCustomer() {
        return findCustomer;
    }

    public ListCustomers getListCustomers() {
        return listCustomers;
    }

    public UpdateCustomerEmail getUpdateCustomerEmail() {
        return updateCustomerEmail;
    }

    public RegisterBook getRegisterBook() {
        return registerBook;
    }

    public FindBook getFindBook() {
        return findBook;
    }

    public ListBooks getListBooks() {
        return listBooks;
    }

    public CreateLoan getCreateLoan() {
        return createLoan;
    }

    public SearchLoans getSearchLoans() {
        return searchLoans;
    }

    public ListLoans getListLoans() {
        return listLoans;
    }

    public ReturnLoan getReturnLoan() {
        return returnLoan;
    }

    public CloseOverdueLoan getCloseOverdueLoan() {
        return closeOverdueLoan;
    }
}
