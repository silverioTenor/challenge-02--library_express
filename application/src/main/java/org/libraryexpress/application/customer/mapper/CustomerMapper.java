package org.libraryexpress.application.customer.mapper;

import org.libraryexpress.application.customer.dto.request.CreateCustomerDto;
import org.libraryexpress.application.customer.dto.response.CustomerDto;
import org.libraryexpress.domain.customer.entity.Customer;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;

@Mapper(unmappedTargetPolicy = ReportingPolicy.WARN)
public interface CustomerMapper {

    CustomerMapper INSTANCE = Mappers.getMapper(CustomerMapper.class);

    CustomerDto toResponseDto(Customer customer);

    Customer.Builder toEntity(CreateCustomerDto dto);
}
