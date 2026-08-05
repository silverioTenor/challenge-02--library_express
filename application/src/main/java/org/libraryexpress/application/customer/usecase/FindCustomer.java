package org.libraryexpress.application.customer.usecase;

import org.libraryexpress.application.customer.dto.response.CustomerDto;
import org.libraryexpress.domain.customer.exception.CustomerNotFoundException;
import org.libraryexpress.application.customer.mapper.CustomerMapper;
import org.libraryexpress.domain.customer.entity.Customer;
import org.libraryexpress.domain.customer.repository.CustomerRepository;

import java.util.Optional;

public class FindCustomer {

    private final CustomerRepository customerRepository;
    private final CustomerMapper mapper;

    public FindCustomer(CustomerRepository customerRepository, CustomerMapper mapper) {
        this.customerRepository = customerRepository;
        this.mapper = mapper;
    }

    public CustomerDto execute(String emailOrId) throws CustomerNotFoundException {

        Optional<Customer> customer = emailOrId.contains("@")
                ? this.customerRepository.getByEmail(emailOrId)
                : this.customerRepository.getById(emailOrId);

        return customer
                .map(mapper::toResponseDto)
                .orElseThrow(CustomerNotFoundException::new);
    }
}
