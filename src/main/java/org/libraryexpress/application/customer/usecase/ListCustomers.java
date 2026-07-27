package org.libraryexpress.application.customer.usecase;

import org.libraryexpress.application.customer.mapper.CustomerMapper;
import org.libraryexpress.domain.entity.Customer;
import org.libraryexpress.domain.repository.ICustomerRepository;
import org.libraryexpress.infrastructure.repository.CustomerRepository;

import java.util.Set;

public class ListCustomers {

    private final ICustomerRepository customerRepository;
    private final CustomerMapper mapper;

    public ListCustomers(ICustomerRepository customerRepository, CustomerMapper mapper) {
        this.customerRepository = customerRepository;
        this.mapper = mapper;
    }

    public Set<Customer> execute() {
        return this.customerRepository.all().orElse(null);
    }
}
