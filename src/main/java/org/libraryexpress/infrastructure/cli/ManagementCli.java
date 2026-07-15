package org.libraryexpress.infrastructure.cli;

import java.util.Scanner;

public class ManagementCli {

    private final CustomerCli customerCli;
    private final BookCli bookCli;
    private final LoanCli loanCli;

    public ManagementCli() {
        this.customerCli = new CustomerCli();
        this.bookCli = new BookCli();
        this.loanCli = new LoanCli();
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
                case 1 -> this.loanCli.init(scan);
                case 2 -> this.bookCli.init(scan);
                case 3 -> this.customerCli.init(scan);
                case 4, 5 -> System.out.println("Option currently unavailable");
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
