package org.libraryexpress.application.customer.usecase;

import org.libraryexpress.application.customer.dto.request.CreateCustomerDto;
import org.libraryexpress.application.customer.exception.UniqueEmailException;
import org.libraryexpress.application.customer.mapper.CustomerMapper;
import org.libraryexpress.domain.entity.Customer;
import org.libraryexpress.domain.repository.CustomerRepository;

import java.util.Optional;

public class CreateCustomer {

    private final CustomerRepository customerRepository;
    private final CustomerMapper mapper;

    public CreateCustomer(CustomerRepository customerRepository, CustomerMapper mapper) {
        this.customerRepository = customerRepository;
        this.mapper = mapper;
    }

    public void execute(CreateCustomerDto createCustomerDto) throws UniqueEmailException {

        Optional<Customer> hasClient = this.customerRepository.getByEmail(createCustomerDto.email());

        if (hasClient.isPresent()) throw new UniqueEmailException();

        Customer customer = mapper.toEntity(createCustomerDto).build();

        this.customerRepository.create(customer);
    }
}
