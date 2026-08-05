package org.libraryexpress.application.loan.mapper;

import org.libraryexpress.application.loan.dto.response.LoanDto;
import org.libraryexpress.domain.book.valueobject.Isbn;
import org.libraryexpress.domain.loan.entity.Loan;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;

@Mapper(unmappedTargetPolicy = ReportingPolicy.WARN)
public interface LoanMapper {
    LoanMapper INSTANCE = Mappers.getMapper((LoanMapper.class));

    LoanDto toResponseDto(Loan loan);

    Loan.Builder toEntity(LoanDto dto);

    default Isbn mapStringToIsbn(String value) {
        return value != null ? new Isbn(value) : null;
    }

    default String mapIsbnToString(Isbn isbn) {
        return isbn != null ? isbn.value() : null;
    }
}
