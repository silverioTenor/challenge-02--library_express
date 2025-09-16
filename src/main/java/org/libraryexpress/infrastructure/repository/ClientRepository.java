package org.libraryexpress.infrastructure.repository;

import org.libraryexpress.domain.entity.Client;
import org.libraryexpress.domain.repository.IClientRepository;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

public enum ClientRepository implements IClientRepository {
    DB;

    private final Set<Client> group = new HashSet<>();

    @Override
    public void create(Client client) {
        group.add(client);
    }

    @Override
    public boolean update(String id, String email) {
        return group.stream()
                .filter(client -> client.getID().equals(id))
                .findFirst()
                .map(client -> {
                    client.changeEmail(email);
                    return true;
                })
                .orElse(false);

    }

    @Override
    public Optional<Client> getById(String id) {
        return group.stream()
                .filter(client -> client.getID().equals(id))
                .findFirst();
    }

    @Override
    public Optional<Client> getByEmail(String email) {
        return group.stream()
                .filter(client -> client.getEmail().equals(email))
                .findFirst();
    }

    @Override
    public Optional<Set<Client>> all() {
        return Optional.of(group);
    }
}
