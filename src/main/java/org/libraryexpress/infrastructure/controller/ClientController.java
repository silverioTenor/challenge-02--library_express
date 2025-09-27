package org.libraryexpress.infrastructure.controller;

import org.libraryexpress.domain.entity.Client;
import org.libraryexpress.infrastructure.exception.RuleViolationException;
import org.libraryexpress.infrastructure.service.ClientService;

import java.util.Objects;
import java.util.Scanner;

class ClientController {

    private final ClientService clientService;

    public ClientController() {
        clientService = new ClientService();
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
                case 2 -> this.show(scan);
                case 3 -> this.update(scan);
                case 4 -> this.list();
                case 6 -> loop = false;
                default -> System.out.println("Invalid option!");
            }

        } while (loop);
    }

    private void create(Scanner scan) {
        try {

            this.clientService.register(scan);

            System.out.println(" ");
            System.out.println("Client registered successfully!");

        } catch (RuleViolationException e) {

            System.out.println(e.getMessage());

        } catch (Exception e) {
            System.out.println(" ");
            System.out.println("Unexpected error has occurred:");
            System.out.println(e.getMessage());
        }
    }

    private void show(Scanner scan) {
        Client client;

        System.out.println("Enter the client's mail or ID");
        String dataToSearch = scan.next();

        if (dataToSearch.contains("@")) {
            client = this.clientService.findByEmail(dataToSearch);
        } else {
            client = this.clientService.findById(dataToSearch);
        }

        String dataToView = Objects.isNull(client)
                ? "Client not found!"
                : client.toString();

        System.out.println(dataToView);
    }

    private void update(Scanner scan) {
        System.out.println("Enter the client's ID");
        String id = scan.next();

        Client foundClient = this.clientService.findById(id);

        if (Objects.isNull(foundClient)) {
            System.out.println("Client not found!");
        } else {
            System.out.println("Enter the new client's e-mail");
            String email = scan.next();

            boolean result = this.clientService.update(id, email);

            String dataToView = result ? "Update success!" : "Failed to update";

            System.out.println(dataToView);
        }
    }

    private void list() {
        var clients = this.clientService.list();

        if (clients.isEmpty()) {
            System.out.println("No clients found.");
        } else {
            clients.forEach(System.out::println);
        }
    }
}
