package org.libraryexpress.infrastructure.controller;

import org.libraryexpress.domain.entity.Book;
import org.libraryexpress.domain.entity.Client;
import org.libraryexpress.domain.entity.Loan;
import org.libraryexpress.domain.entity.WaitingList;
import org.libraryexpress.infrastructure.exception.BookUnavailableException;
import org.libraryexpress.infrastructure.exception.NotFoundException;
import org.libraryexpress.infrastructure.service.BookService;
import org.libraryexpress.infrastructure.service.ClientService;
import org.libraryexpress.infrastructure.service.LoanService;

import java.util.Scanner;

class LoanController {

    private final BookService bookService;

    private final ClientService clientService;

    private final LoanService loanService;

    public LoanController() {
        bookService = new BookService();
        clientService = new ClientService();
        loanService = new LoanService();
    }

    public void init(Scanner scan) {
        var loop = true;

        do {
            System.out.println(" ");
            System.out.println("[1] - New");
//            System.out.println("[2] - Book");
//            System.out.println("[3] - Client");
            System.out.println("[6] - Return");
            System.out.println(" ");

            int option = scan.nextInt();

            switch (option) {
                case 1 -> this.subscribe(scan);
                case 6 -> loop = false;
                default -> System.out.println("Invalid option!");
            }

        } while (loop);
    }

    private void subscribe(Scanner scan) {
        String isbn = "";
        String email = "";

        try {
            System.out.println("Enter the client's e-mail");
            email = scan.next();

            Client client = this.clientService.findByEmail(email);

            System.out.println(" ");
            System.out.println("Enter the ISBN");
            isbn = scan.next();

            Book book = this.bookService.findByIsbn(isbn);

            Loan loan = this.loanService.subscribe(client, book);

            System.out.println(" ");
            System.out.println("Loan made successfully!");
            System.out.println("Attention to delivery date on " + loan.getDeliveryDate().toString());

        } catch (NotFoundException e) {

            System.out.println("Only registered users can apply for loans");

        } catch (BookUnavailableException e) {

            System.out.println(" ");
            System.out.println("Do you want to join the waiting list?");
            String response = scan.next().toLowerCase();

            if (response.equals("y") && (!isbn.isEmpty() && !email.isEmpty())) {
                WaitingList waitingList = new WaitingList.Builder()
                        .setISBN(isbn)
                        .setClientEmail(email)
                        .build();

                System.out.println("Success!");
                System.out.println("You'll be notified when the book is available again.");

            }

        } catch (Exception e) {
            System.out.println(" ");
            System.out.println("Unexpected error has occurred:");
            System.out.println(e.getMessage());
        }
    }
}
