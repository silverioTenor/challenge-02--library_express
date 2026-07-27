package org.libraryexpress.application.customer.usecase;

import org.libraryexpress.application.customer.dto.request.CreateCustomerDto;
import org.libraryexpress.application.customer.mapper.CustomerMapper;
import org.libraryexpress.domain.entity.Customer;
import org.libraryexpress.domain.repository.ICustomerRepository;
import org.libraryexpress.infrastructure.exception.RuleViolationException;
import org.libraryexpress.infrastructure.repository.CustomerRepository;

import java.util.Optional;

public class CreateCustomer {

    private final ICustomerRepository customerRepository;
    private final CustomerMapper mapper;

    public CreateCustomer(ICustomerRepository customerRepository, CustomerMapper mapper) {
        this.customerRepository = customerRepository;
        this.mapper = mapper;
    }

    public void execute(CreateCustomerDto createCustomerDto) throws RuleViolationException {

        Optional<Customer> hasClient = this.customerRepository.getByEmail(createCustomerDto.email());

        if (hasClient.isPresent()) throw new RuleViolationException("E-mail must be unique.");

        Customer customer = mapper.toEntity(createCustomerDto).build();

        this.customerRepository.create(customer);
    }
}
