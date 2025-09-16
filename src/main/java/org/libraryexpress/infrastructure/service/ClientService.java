package org.libraryexpress.infrastructure.service;

import org.libraryexpress.domain.entity.Client;
import org.libraryexpress.domain.repository.IClientRepository;
import org.libraryexpress.infrastructure.repository.ClientRepository;

import java.util.Optional;
import java.util.Scanner;

public class ClientService {

    private final IClientRepository clientRepository;

    public ClientService() {
        this.clientRepository = ClientRepository.DB;
    }

    public Client register() {
        Scanner scan = new Scanner(System.in);
        boolean emailIsInvalid = true;

        System.out.println("  ");
        System.out.println("Enter with name:");
        String name = scan.next();
        String email;
        System.out.println("  ");

        do {
            System.out.println("  ");
            System.out.println("Enter with email:");
            email = scan.next();
            System.out.println("  ");

            Optional<Client> hasClient = this.clientRepository.getByEmail(email);

            if (hasClient.isPresent()) emailIsInvalid = false;
        } while (emailIsInvalid);

        Client client = new Client.Builder()
                .setName(name)
                .setEmail(email)
                .build();

        scan.close();

        this.clientRepository.create(client);
        return client;
    }

    public boolean update(String id, String email) {
        return this.clientRepository.update(id, email);
    }

    public Client findById(String id) {
        return this.clientRepository.getById(id).orElse(null);
    }

    public Client findByEmail(String email) {
        return this.clientRepository.getByEmail(email).orElse(null);
    }
}
