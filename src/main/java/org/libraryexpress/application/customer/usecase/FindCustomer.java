package org.libraryexpress.application.customer.usecase;

import org.libraryexpress.application.customer.dto.response.CustomerDto;
import org.libraryexpress.application.customer.mapper.CustomerMapper;
import org.libraryexpress.domain.entity.Customer;
import org.libraryexpress.domain.repository.ICustomerRepository;
import org.libraryexpress.infrastructure.exception.NotFoundException;
import org.libraryexpress.infrastructure.repository.CustomerRepository;

import java.util.Optional;

public class FindCustomer {

    private final ICustomerRepository customerRepository;
    private final CustomerMapper mapper;

    public FindCustomer(ICustomerRepository customerRepository, CustomerMapper mapper) {
        this.customerRepository = customerRepository;
        this.mapper = mapper;
    }

    public CustomerDto execute(String emailOrId) throws NotFoundException {

        Optional<Customer> customer = emailOrId.contains("@")
                ? this.customerRepository.getByEmail(emailOrId)
                : this.customerRepository.getById(emailOrId);

        return customer
                .map(mapper::toResponseDto)
                .orElseThrow(() -> new NotFoundException("Customer not found!"));
    }
}
