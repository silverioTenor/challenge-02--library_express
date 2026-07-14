package org.libraryexpress.infrastructure.cli;

import java.util.Scanner;

public class ManagementCli {

//    private final LoanController loanController;

    private final CustomerCli customerCli;

    public ManagementCli() {
//        loanController = new LoanController();
        customerCli = new CustomerCli();
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
            System.out.println("[3] - Customer");
            System.out.println("[6] - Exit");
            System.out.println(" ");

            int option = scan.nextInt();

            switch (option) {
//                case 1 -> this.loanController.init(scan);
                case 1, 2, 4, 5 -> System.out.println("Option currently unavailable");
                case 3 -> this.customerCli.init(scan);
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
