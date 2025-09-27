package org.libraryexpress.infrastructure.service;

import org.libraryexpress.domain.entity.Client;
import org.libraryexpress.domain.repository.IClientRepository;
import org.libraryexpress.infrastructure.exception.NotFoundException;
import org.libraryexpress.infrastructure.exception.RuleViolationException;
import org.libraryexpress.infrastructure.repository.ClientRepository;

import java.util.Optional;
import java.util.Scanner;
import java.util.Set;

public class ClientService {

    private final IClientRepository clientRepository;

    public ClientService() {
        this.clientRepository = ClientRepository.DB;
    }

    public void register(Scanner scan) throws RuleViolationException {
        System.out.println("Enter with name:");
        String name = scan.next();
        System.out.println("  ");

        System.out.println("Enter with email:");
        String email = scan.next();
        System.out.println("  ");

        Optional<Client> hasClient = this.clientRepository.getByEmail(email);

        if (hasClient.isPresent()) throw new RuleViolationException("E-mail must be unique.");

        Client client = new Client.Builder()
                .setName(name)
                .setEmail(email)
                .build();

        this.clientRepository.create(client);
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

    public Client findByEmailOrFail(String email) throws NotFoundException {
        var result = this.clientRepository.getByEmail(email);

        if (result.isEmpty()) throw new NotFoundException("Client not found!");

        return result.get();
    }

    public Set<Client> list() {
        return this.clientRepository.all().orElse(null);
    }
}
