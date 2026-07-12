package org.libraryexpress.application.customer.usecase;

import org.libraryexpress.application.customer.mapper.CustomerMapper;
import org.libraryexpress.domain.entity.Customer;
import org.libraryexpress.domain.repository.ICustomerRepository;
import org.libraryexpress.infrastructure.exception.NotFoundException;
import org.libraryexpress.infrastructure.repository.CustomerRepository;

public class FindCustomer {

    private final ICustomerRepository customerRepository;
    private final CustomerMapper mapper;

    public FindCustomer() {
        this.customerRepository = CustomerRepository.DB;
        this.mapper = CustomerMapper.INSTANCE;
    }

    public Customer findById(String id) {
        return this.customerRepository.getById(id).orElse(null);
    }

    public Customer findByEmail(String email) {
        return this.customerRepository.getByEmail(email).orElse(null);
    }

    public Customer findByEmailOrFail(String email) throws NotFoundException {
        var result = this.customerRepository.getByEmail(email);

        if (result.isEmpty()) throw new NotFoundException("Customer not found!");

        return result.get();
    }
}
