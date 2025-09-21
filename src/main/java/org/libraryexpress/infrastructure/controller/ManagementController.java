package org.libraryexpress.infrastructure.controller;

import java.util.Scanner;

public class ManagementController {

    private final LoanController loanController;

    public ManagementController() {
        loanController = new LoanController();
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
            System.out.println("[6] - Exit");
            System.out.println(" ");

            int option = scan.nextInt();

            switch (option) {
                case 1 -> this.loanController.init(scan);
                case 2, 3, 4, 5 -> System.out.println("Option currently unavailable");
                case 6 -> {
                    loop = false;
                    System.out.println("Good bye!");
                }
                default -> System.out.println("Invalid option!");
            }
        } while (loop);

        scan.close();
    }
}
