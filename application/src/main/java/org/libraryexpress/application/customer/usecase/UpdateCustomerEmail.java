package org.libraryexpress.application.customer.usecase;

import org.libraryexpress.application.customer.dto.request.UpdateCustomerEmailDto;
import org.libraryexpress.application.customer.exception.CustomerNotFoundException;
import org.libraryexpress.application.customer.mapper.CustomerMapper;
import org.libraryexpress.domain.entity.Customer;
import org.libraryexpress.domain.repository.CustomerRepository;

import java.util.Optional;

public class UpdateCustomerEmail {

    private final CustomerRepository customerRepository;
    private final CustomerMapper mapper;

    public UpdateCustomerEmail(CustomerRepository customerRepository, CustomerMapper mapper) {
        this.customerRepository = customerRepository;
        this.mapper = mapper;
    }

    public void execute(UpdateCustomerEmailDto updateCustomerEmailDto) throws CustomerNotFoundException {

        String id = updateCustomerEmailDto.id();
        String email = updateCustomerEmailDto.email();

        Optional<Customer> customer = this.customerRepository.getById(id);

        if (customer.isEmpty()) {
            throw new CustomerNotFoundException();
        }

        this.customerRepository.update(id, email);
    }
}
