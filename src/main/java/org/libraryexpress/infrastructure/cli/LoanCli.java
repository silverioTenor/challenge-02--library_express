package org.libraryexpress.infrastructure.cli;

import org.libraryexpress.application.book.dto.response.BookDto;
import org.libraryexpress.application.book.usecase.FindBook;
import org.libraryexpress.application.customer.dto.response.CustomerDto;
import org.libraryexpress.application.customer.usecase.FindCustomer;
import org.libraryexpress.application.loan.dto.request.CreateLoanDto;
import org.libraryexpress.application.loan.usecase.CreateLoan;
import org.libraryexpress.infrastructure.exception.NotFoundException;
import org.libraryexpress.infrastructure.exception.RuleViolationException;

import java.util.Scanner;

class LoanCli {

    private final FindCustomer findCustomer;
    private final FindBook findBook;
    private final CreateLoan createLoan;

    public LoanCli() {
        this.findCustomer = new FindCustomer();
        this.findBook = new FindBook();
        this.createLoan = new CreateLoan();
    }

    public void init(Scanner scan) {

        var loop = true;

        do {
            System.out.println(" ");
            System.out.println("[1] - New");
            System.out.println("[2] - Search");
            System.out.println("[3] - List");
            System.out.println("[6] - Return");
            System.out.println(" ");

            int option = scan.nextInt();

            switch (option) {
                case 1 -> this.createLoan(scan);
                case 2 -> this.searchLoan(scan);
                case 3 -> this.listLoans();
                case 4, 5 -> System.out.println("Option currently unavailable");
                case 6 -> loop = false;
                default -> System.out.println("Invalid option!");
            }

        } while (loop);
    }

    public void createLoan(Scanner scan) {

        System.out.println("  ");
        System.out.println("Enter with customer ID");
        String customerId = scan.next();

        System.out.println("  ");
        System.out.println("Enter with ISBN");
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
        } catch (RuleViolationException | NotFoundException e) {
            System.out.println(e.getMessage());
        }
    }

    public void searchLoan(Scanner scan) {}

    public void listLoans() {}

    public void finishLoan() {}
}
