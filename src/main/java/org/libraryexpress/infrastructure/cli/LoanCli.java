package org.libraryexpress.infrastructure.cli;

import org.libraryexpress.application.book.dto.response.BookDto;
import org.libraryexpress.application.book.usecase.FindBook;
import org.libraryexpress.application.customer.dto.response.CustomerDto;
import org.libraryexpress.application.customer.usecase.FindCustomer;
import org.libraryexpress.application.loan.dto.request.CreateLoanDto;
import org.libraryexpress.application.loan.dto.request.FilterLoansDto;
import org.libraryexpress.application.loan.dto.response.LoanDto;
import org.libraryexpress.application.loan.usecase.CreateLoan;
import org.libraryexpress.application.loan.usecase.ListLoans;
//import org.libraryexpress.application.loan.usecase.ReturnLoan;
import org.libraryexpress.application.loan.usecase.SearchLoans;
import org.libraryexpress.domain.enums.LoanStatus;
import org.libraryexpress.infrastructure.exception.NotFoundException;
import org.libraryexpress.infrastructure.exception.RuleViolationException;

import java.util.Scanner;
import java.util.Set;

class LoanCli {

    private final FindCustomer findCustomer;
    private final FindBook findBook;
    private final CreateLoan createLoan;
    private final SearchLoans searchLoans;
    private final ListLoans listLoans;
//    private final ReturnLoan returnLoan;

    public LoanCli() {
        this.findCustomer = new FindCustomer();
        this.findBook = new FindBook();
        this.createLoan = new CreateLoan();
        this.searchLoans = new SearchLoans();
        this.listLoans = new ListLoans();
//        this.returnLoan = new ReturnLoan();
    }

    public void init(Scanner scan) {

        var loop = true;

        do {
            System.out.println(" ");
            System.out.println("[1] - New");
            System.out.println("[2] - Search");
            System.out.println("[3] - Devolution");
            System.out.println("[4] - List");
            System.out.println("[6] - Return");
            System.out.println(" ");

            int option = scan.nextInt();

            switch (option) {
                case 1 -> this.createLoan(scan);
                case 2 -> this.searchLoan(scan);
                case 3 -> this.returnLoan(scan);
                case 4 -> this.listLoans(scan);
                case 5 -> System.out.println("Option currently unavailable");
                case 6 -> loop = false;
                default -> System.out.println("Invalid option!");
            }

        } while (loop);
    }

    public void createLoan(Scanner scan) {

        System.out.println("  ");
        System.out.println("Enter the customer ID");
        String customerId = scan.next();

        System.out.println("  ");
        System.out.println("Enter the ISBN");
        String ISBN = scan.next();

        CustomerDto customer;
        BookDto book;

        try {
            this.findCustomer.execute(customerId);
        } catch (NotFoundException e) {
            System.out.println(e.getMessage());
            return;
        }

        CreateLoanDto createLoanDto = new CreateLoanDto(customerId, ISBN);

        try {
            this.createLoan.execute(createLoanDto);
            System.out.println("Loan realized!");

        } catch (RuleViolationException | NotFoundException e) {
            System.out.println(e.getMessage());
        }
    }

    public void searchLoan(Scanner scan) {

        System.out.println("  ");
        System.out.println("Enter the customer ID");
        String customerId = scan.next();

        System.out.println("  ");
        System.out.println("Enter the ISBN");
        String ISBN = scan.next();

        System.out.println("  ");
        System.out.println("Enter the getStatus");
        LoanStatus status = LoanStatus.valueOf(scan.next().toUpperCase());

        FilterLoansDto filterDto = new FilterLoansDto(customerId, ISBN, status);

        try {
            Set<LoanDto> loans = this.searchLoans.execute(filterDto);
        } catch (RuleViolationException e) {
            System.out.println(e.getMessage());
        }
    }

    public void returnLoan(Scanner scan) {

        System.out.println("  ");
        System.out.println("Enter the customer's ID");
        String customerId = scan.next();

        System.out.println("  ");
        System.out.println("Enter the ISBN");
        String ISBN = scan.next();

        FilterLoansDto filter = new FilterLoansDto(customerId, ISBN, LoanStatus.ACTIVE);

//        try {
//            this.returnLoan.execute(filter);
//        } catch (RuleViolationException | NotFoundException e) {
//            System.out.println(e.getMessage());
//        }
    }

    public void listLoans(Scanner scan) {

        Set<LoanDto> loans = this.listLoans.execute();
        loans.forEach(System.out::println);
    }
}
