package org.libraryexpress.infrastructure.repository.InMemory;

import org.libraryexpress.domain.customer.entity.Customer;
import org.libraryexpress.domain.customer.repository.CustomerRepository;

import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

public class InMemoryCustomerRepository implements CustomerRepository {

    private final Map<String, Customer> group = new ConcurrentHashMap<>();

    @Override
    public void create(Customer customer) {
        group.put(customer.getId(), customer);
    }

    @Override
    public void update(Customer customerToUpdate) {
        Optional.ofNullable(group.get(customerToUpdate.getId()))
                .ifPresent(customer -> customer.changeEmail(customerToUpdate.getEmail().value()));
    }

    @Override
    public Optional<Customer> getById(String id) {
        return Optional.ofNullable(group.get(id));
    }

    @Override
    public Optional<Customer> getByEmail(String email) {
        return group.values().stream()
                .filter(client -> client.getEmail().value().equals(email))
                .findFirst();
    }

    @Override
    public Set<Customer> all() {
        return Set.copyOf(group.values());
    }
}
