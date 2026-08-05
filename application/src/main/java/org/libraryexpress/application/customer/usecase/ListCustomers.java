package org.libraryexpress.application.customer.usecase;

import org.libraryexpress.application.customer.mapper.CustomerMapper;
import org.libraryexpress.domain.customer.entity.Customer;
import org.libraryexpress.domain.customer.repository.CustomerRepository;

import java.util.Set;

public class ListCustomers {

    private final CustomerRepository customerRepository;
    private final CustomerMapper mapper;

    public ListCustomers(CustomerRepository customerRepository, CustomerMapper mapper) {
        this.customerRepository = customerRepository;
        this.mapper = mapper;
    }

    public Set<Customer> execute() {
        return this.customerRepository.all();
    }
}
