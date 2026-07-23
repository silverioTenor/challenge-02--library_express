package org.libraryexpress.domain.repository;

import org.libraryexpress.domain.entity.Customer;

import java.util.Optional;
import java.util.Set;

public interface ICustomerRepository {

    void create(Customer customer);
    void update(String id, String email);

    Optional<Customer> getById(String id);
    Optional<Customer> getByEmail(String email);
    Optional<Set<Customer>> all();
}
