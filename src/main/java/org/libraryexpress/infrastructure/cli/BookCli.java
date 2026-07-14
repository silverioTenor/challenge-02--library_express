package org.libraryexpress.infrastructure.cli;

import org.libraryexpress.application.book.dto.RegisterBookDto;

import java.util.Scanner;

public class BookCli {

    public BookCli() {}

    public void init(Scanner scan) {

        boolean loop = true;

        do {
            System.out.println(" ");
            System.out.println("[1] - Register");
            System.out.println("[2] - Show");
            System.out.println("[3] - Update");
            System.out.println("[4] - List");
            System.out.println("[6] - Return");
            System.out.println(" ");

            int option = scan.nextInt();

            switch (option) {
                case 1 -> this.create(scan);
//                case 2 -> this.show(scan);
//                case 3 -> this.update(scan);
//                case 4 -> this.list();
                case 6 -> loop = false;
                default -> System.out.println("Invalid option!");
            }

        } while (loop);

    }

    private void create(Scanner scan) {

        System.out.println("  ");
        System.out.println("Enter with title:");
        String title = scan.next();

        System.out.println("  ");
        System.out.println("Enter with author:");
        String author = scan.next();

        System.out.println("  ");
        System.out.println("Enter with year:");
        int year = scan.nextInt();

        RegisterBookDto registerBookDto = new RegisterBookDto(title, author, year);
    }
}
