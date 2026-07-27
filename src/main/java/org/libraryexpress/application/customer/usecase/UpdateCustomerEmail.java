package org.libraryexpress.application.customer.usecase;

import org.libraryexpress.application.customer.dto.request.UpdateCustomerEmailDto;
import org.libraryexpress.application.customer.mapper.CustomerMapper;
import org.libraryexpress.domain.entity.Customer;
import org.libraryexpress.domain.repository.ICustomerRepository;
import org.libraryexpress.infrastructure.exception.NotFoundException;
import org.libraryexpress.infrastructure.repository.CustomerRepository;

import java.util.Optional;

public class UpdateCustomerEmail {

    private final ICustomerRepository customerRepository;
    private final CustomerMapper mapper;

    public UpdateCustomerEmail(ICustomerRepository customerRepository, CustomerMapper mapper) {
        this.customerRepository = customerRepository;
        this.mapper = mapper;
    }

    public void execute(UpdateCustomerEmailDto updateCustomerEmailDto) throws NotFoundException {

        String id = updateCustomerEmailDto.id();
        String email = updateCustomerEmailDto.email();

        Optional<Customer> customer = this.customerRepository.getById(updateCustomerEmailDto.id());

        if (customer.isEmpty()) {
            throw new NotFoundException("Customer not found!");
        }

        this.customerRepository.update(id, email);
    }
}
