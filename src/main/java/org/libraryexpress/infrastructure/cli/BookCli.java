package org.libraryexpress.infrastructure.cli;

import org.libraryexpress.application.book.dto.request.RegisterBookDto;
import org.libraryexpress.application.book.dto.response.BookDto;
import org.libraryexpress.application.book.mapper.BookMapper;
import org.libraryexpress.application.book.usecase.FindBook;
import org.libraryexpress.application.book.usecase.ListBooks;
import org.libraryexpress.application.book.usecase.RegisterBook;
import org.libraryexpress.domain.enums.BookStatus;
import org.libraryexpress.domain.helper.Generator;
import org.libraryexpress.infrastructure.exception.NotFoundException;
import org.libraryexpress.infrastructure.exception.RuleViolationException;
import org.libraryexpress.infrastructure.repository.BookRepository;

import java.util.Scanner;

public class BookCli {

    private final RegisterBook registerBook;
    private final FindBook findBook;
    private final ListBooks listBooks;

    public BookCli() {
        this.registerBook = new RegisterBook(BookRepository.DB, BookMapper.INSTANCE);
        this.findBook = new FindBook(BookRepository.DB, BookMapper.INSTANCE);
        this.listBooks = new ListBooks(BookRepository.DB);
    }

    public void init(Scanner scan) {

        boolean loop = true;

        do {
            System.out.println(" ");
            System.out.println("[1] - Register");
            System.out.println("[2] - Show");
            System.out.println("[3] - List");
            System.out.println("[6] - Back");
            System.out.println(" ");

            int option = scan.nextInt();

            switch (option) {
                case 1 -> this.register(scan);
                case 2 -> this.show(scan);
                case 3 -> this.list(scan);
                case 6 -> loop = false;
                default -> System.out.println("Invalid option!");
            }

        } while (loop);

    }

    private void register(Scanner scan) {

        String ISBN = Generator.genISBN();

        scan.nextLine();

        System.out.println("Enter with title:");
        String title = scan.nextLine();
        System.out.println("  ");

        System.out.println("Enter with author:");
        String author = scan.nextLine();
        System.out.println("  ");

        System.out.println("Enter with year:");
        int year;
        try {
            year = Integer.parseInt(scan.nextLine().trim());
        } catch (NumberFormatException e) {
            System.out.println("Invalid year. Registration cancelled.");
            return;
        }

        RegisterBookDto registerBookDto = new RegisterBookDto(ISBN, title, author, year, BookStatus.AVAILABLE);

        try {
            this.registerBook.execute(registerBookDto);

            System.out.println("Book registered successfully");

        } catch (RuleViolationException e) {
            System.out.println(e.getMessage());
        }
    }

    private void show(Scanner scan) {

        System.out.println("  ");
        System.out.println("Enter with ISBN:");
        String ISBN = scan.next();

        try {
            BookDto bookDto = this.findBook.execute(ISBN);

            System.out.println(bookDto);
        } catch (NotFoundException e) {
            System.out.println(e.getMessage());
        }
    }

    private void list(Scanner scan) {

        var books = this.listBooks.execute();

        if (books.isEmpty()) {
            System.out.println("No books found.");
        } else {
            books.forEach(System.out::println);
        }
    }
}
