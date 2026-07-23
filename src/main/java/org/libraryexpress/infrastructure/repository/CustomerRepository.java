package org.libraryexpress.infrastructure.repository;

import org.libraryexpress.domain.entity.Customer;
import org.libraryexpress.domain.repository.ICustomerRepository;

import java.util.Optional;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

public enum CustomerRepository implements ICustomerRepository {
    DB;

    private final Set<Customer> group = ConcurrentHashMap.newKeySet();

    @Override
    public void create(Customer customer) {
        group.add(customer);
    }

    @Override
    public synchronized void update(String id, String email) {
        group.stream()
                .filter(client -> client.getID().equals(id))
                .findFirst()
                .map(client -> {
                    client.changeEmail(email);
                    return true;
                });

    }

    @Override
    public Optional<Customer> getById(String id) {
        return group.stream()
                .filter(client -> client.getID().equals(id))
                .findFirst();
    }

    @Override
    public Optional<Customer> getByEmail(String email) {
        return group.stream()
                .filter(client -> client.getEmail().equals(email))
                .findFirst();
    }

    @Override
    public Optional<Set<Customer>> all() {
        return Optional.of(group);
    }
}
