package org.libraryexpress.infrastructure.repository.InMemory;

import org.libraryexpress.domain.entity.Customer;
import org.libraryexpress.domain.repository.CustomerRepository;

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
    public void update(String id, String email) {
        Optional.ofNullable(group.get(id))
                .ifPresent(customer -> customer.changeEmail(email));
    }

    @Override
    public Optional<Customer> getById(String id) {
        return Optional.ofNullable(group.get(id));
    }

    @Override
    public Optional<Customer> getByEmail(String email) {
        return group.values().stream()
                .filter(client -> client.getEmail().equals(email))
                .findFirst();
    }

    @Override
    public Set<Customer> all() {
        return Set.copyOf(group.values());
    }
}
