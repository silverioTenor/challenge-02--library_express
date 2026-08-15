package org.libraryexpress.domain.customer.repository;

import org.libraryexpress.domain.customer.entity.Customer;

import java.util.Optional;
import java.util.Set;

public interface CustomerRepository {
    void create(Customer customer);
    void update(Customer customerToUpdate);
    Optional<Customer> getById(String id);
    Optional<Customer> getByEmail(String email);
    Set<Customer> all();
}
