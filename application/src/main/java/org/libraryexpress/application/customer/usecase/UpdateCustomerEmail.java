package org.libraryexpress.application.customer.usecase;

import org.libraryexpress.application.customer.dto.request.UpdateCustomerEmailDto;
import org.libraryexpress.domain.customer.exception.CustomerNotFoundException;
import org.libraryexpress.application.customer.mapper.CustomerMapper;
import org.libraryexpress.domain.customer.entity.Customer;
import org.libraryexpress.domain.customer.repository.CustomerRepository;

import java.util.Optional;

public class UpdateCustomerEmail {

    private final CustomerRepository customerRepository;
    private final CustomerMapper mapper;

    public UpdateCustomerEmail(CustomerRepository customerRepository, CustomerMapper mapper) {
        this.customerRepository = customerRepository;
        this.mapper = mapper;
    }

    public void execute(UpdateCustomerEmailDto updateCustomerEmailDto) {

        Customer customer = this.customerRepository.getById(updateCustomerEmailDto.id())
                .orElseThrow(CustomerNotFoundException::new);

        customer.changeEmail(updateCustomerEmailDto.email());

        this.customerRepository.update(customer);
    }
}
