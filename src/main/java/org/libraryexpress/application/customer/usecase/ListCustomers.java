package org.libraryexpress.application.customer.usecase;

import org.libraryexpress.domain.entity.Customer;
import org.libraryexpress.domain.repository.ICustomerRepository;
import org.libraryexpress.infrastructure.repository.CustomerRepository;

import java.util.Set;

public class ListCustomers {

    private final ICustomerRepository customerRepository;

    public ListCustomers() {
        this.customerRepository = CustomerRepository.DB;
    }

    public Set<Customer> execute() {
        return this.customerRepository.all().orElse(null);
    }
}
