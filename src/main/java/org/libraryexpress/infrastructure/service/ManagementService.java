package org.libraryexpress.infrastructure.service;

import java.util.Scanner;

public class ManagementService {

    private final BookService bookService;

    private final ClientService clientService;

    private final LoanService loanService;

    public ManagementService() {
        bookService = new BookService();
        clientService = new ClientService();
        loanService = new LoanService();
    }

    public void app() {
        Scanner scan = new Scanner(System.in);
        boolean loop = true;

        System.out.println("=================================");
        System.out.println("Welcome to Library Express System");
        System.out.println("=================================");

        do {
            System.out.println(" ");
            System.out.println("[1] - Loan");
            System.out.println("[2] - Book");
            System.out.println("[3] - Client");
            System.out.println("[4] - Waiting list");
            System.out.println("[5] - Devolution");
            System.out.println("[6] - Exit");
            System.out.println(" ");

            int option = scan.nextInt();

            switch (option) {
                case 1, 2, 3, 4, 5 -> System.out.println("Option currently unavailable");
                case 6 -> loop = false;
                default -> System.out.println("Invalid option!");
            }
        } while (loop);
    }
}
