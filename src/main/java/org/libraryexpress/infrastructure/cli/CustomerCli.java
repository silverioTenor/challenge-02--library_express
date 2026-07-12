package org.libraryexpress.infrastructure.cli;

import org.libraryexpress.application.customer.dto.request.FindCustomerDto;
import org.libraryexpress.application.customer.dto.request.UpdateCustomerEmailDto;
import org.libraryexpress.application.customer.usecase.CreateCustomer;
import org.libraryexpress.application.customer.usecase.FindCustomer;
import org.libraryexpress.application.customer.usecase.ListCustomers;
import org.libraryexpress.application.customer.usecase.UpdateCustomerEmail;
import org.libraryexpress.domain.entity.Customer;
import org.libraryexpress.application.customer.dto.request.CreateCustomerDto;
import org.libraryexpress.infrastructure.exception.NotFoundException;
import org.libraryexpress.infrastructure.exception.RuleViolationException;

import java.util.Objects;
import java.util.Scanner;

class CustomerCli {

    private final CreateCustomer createCustomer;
    private final FindCustomer findCustomer;
    private final ListCustomers listCustomers;
    private final UpdateCustomerEmail updateEmail;

    public CustomerCli() {
        this.createCustomer = new CreateCustomer();
        this.findCustomer = new FindCustomer();
        this.listCustomers = new ListCustomers();
        this.updateEmail = new UpdateCustomerEmail();
    }

    public void init(Scanner scan) {
        var loop = true;

        do {
            System.out.println(" ");
            System.out.println("[1] - New");
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
                case 4 -> this.list();
                case 6 -> loop = false;
                default -> System.out.println("Invalid option!");
            }

        } while (loop);
    }

    private void create(Scanner scan) {
        System.out.println("Enter with name:");
        String name = scan.next();
        System.out.println("  ");

        System.out.println("Enter with email:");
        String email = scan.next();
        System.out.println("  ");

        CreateCustomerDto createCustomerDto = new CreateCustomerDto(name, email);

        try {

            this.createCustomer.execute(createCustomerDto);

            System.out.println(" ");
            System.out.println("Customer registered successfully!");

        } catch (RuleViolationException e) {

            System.out.println(e.getMessage());

        } catch (Exception e) {
            System.out.println(" ");
            System.out.println("Unexpected error has occurred:");
            System.out.println(e.getMessage());
        }
    }

    private void show(FindCustomerDto findCustomerDto) {
        Customer customer;

        System.out.println("Enter the customer's mail or ID");
        String dataToSearch = findCustomerDto.emailOrId();

        if (dataToSearch.contains("@")) {
            customer = this.findCustomer.findByEmail(dataToSearch);
        } else {
            customer = this.findCustomer.findById(dataToSearch);
        }

        String dataToView = Objects.isNull(customer)
                ? "Customer not found!"
                : customer.toString();

        System.out.println(dataToView);
    }

    private void update(Scanner scan) {
        System.out.println("Enter the client's ID");
        String id = scan.next();

        Customer foundCustomer = this.findCustomer.findById(id);

        if (Objects.isNull(foundCustomer)) {
            System.out.println("Customer not found!");
        } else {
            System.out.println("Enter the new client's e-mail");
            String email = scan.next();

            UpdateCustomerEmailDto updateEmail = new UpdateCustomerEmailDto(foundCustomer.getID(), email);

            try {
                this.updateEmail.execute(updateEmail);
            } catch (NotFoundException e) {
                System.out.println(e.getMessage());
                return;
            } catch (Exception e) {
                System.out.println(" ");
                System.out.println("Unexpected error has occurred:");
                System.out.println(e.getMessage());
                return;
            }

            System.out.println("Update success!");
        }
    }

    private void list() {
        var clients = this.listCustomers.execute();

        if (clients.isEmpty()) {
            System.out.println("No clients found.");
        } else {
            clients.forEach(System.out::println);
        }
    }
}
