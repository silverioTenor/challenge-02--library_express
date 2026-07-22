package org.libraryexpress.infrastructure.cli;

import org.libraryexpress.application.book.dto.response.BookDto;
import org.libraryexpress.application.customer.dto.response.CustomerDto;
import org.libraryexpress.application.customer.usecase.FindCustomer;
import org.libraryexpress.application.loan.dto.request.CreateLoanDto;
import org.libraryexpress.application.loan.dto.request.FilterLoansDto;
import org.libraryexpress.application.loan.dto.response.LoanDto;
import org.libraryexpress.application.loan.usecase.*;
import org.libraryexpress.domain.enums.LoanStatus;
import org.libraryexpress.infrastructure.exception.NotFoundException;
import org.libraryexpress.infrastructure.exception.RuleViolationException;
import org.libraryexpress.infrastructure.util.JsonPrinter;

import java.util.Scanner;
import java.util.Set;

class LoanCli {

    private final FindCustomer findCustomer;
    private final CreateLoan createLoan;
    private final SearchLoans searchLoans;
    private final ListLoans listLoans;
    private final ReturnLoan returnLoan;
    private final CloseOverdueLoan closeOverdueLoan;

    public LoanCli() {
        this.findCustomer = new FindCustomer();
        this.createLoan = new CreateLoan();
        this.searchLoans = new SearchLoans();
        this.listLoans = new ListLoans();
        this.returnLoan = new ReturnLoan();
        this.closeOverdueLoan = new CloseOverdueLoan();
    }

    public void init(Scanner scan) {

        var loop = true;

        do {
            System.out.println(" ");
            System.out.println("[1] - New");
            System.out.println("[2] - Search");
            System.out.println("[3] - Devolution");
            System.out.println("[4] - Close Overdue Loan");
            System.out.println("[5] - List");
            System.out.println("[6] - Back");
            System.out.println(" ");

            int option = scan.nextInt();

            switch (option) {
                case 1 -> this.createLoan(scan);
                case 2 -> this.searchLoan(scan);
                case 3 -> this.returnLoan(scan);
                case 4 -> this.closeOverdueLoan(scan);
                case 5 -> this.listLoans(scan);
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

            System.out.println("  ");
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

            System.out.println(JsonPrinter.print(loans));

        } catch (RuleViolationException e) {
            System.out.println(e.getMessage());
        }
    }

    public void returnLoan(Scanner scan) {

        System.out.println("  ");
        System.out.println("Enter the loan's ID");
        String loanId = scan.next();

        try {
            this.returnLoan.execute(loanId);

            System.out.println("  ");
            System.out.println("Loan successfully completed!");

        } catch (RuleViolationException | NotFoundException e) {
            System.out.println(e.getMessage());
        }
    }

    public void closeOverdueLoan(Scanner scan) {

        System.out.println("  ");
        System.out.println("Enter the loan ID");
        String loanId = scan.next();

        try {
            this.closeOverdueLoan.execute(loanId);
            System.out.println("  ");
            System.out.println("Loan finished successfully!");

        } catch (NotFoundException | RuleViolationException e) {
            System.out.println(e.getMessage());
        }
    }

    public void listLoans(Scanner scan) {

        Set<LoanDto> loans = this.listLoans.execute();

        System.out.println(JsonPrinter.print(loans));
    }
}
