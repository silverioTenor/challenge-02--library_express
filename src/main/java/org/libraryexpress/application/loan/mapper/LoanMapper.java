package org.libraryexpress.application.loan.mapper;

import org.libraryexpress.application.loan.dto.response.LoanDto;
import org.libraryexpress.domain.entity.Loan;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;

@Mapper(unmappedTargetPolicy = ReportingPolicy.WARN)
public interface LoanMapper {
    LoanMapper INSTANCE = Mappers.getMapper((LoanMapper.class));

    LoanDto toResponseDto(Loan loan);

    Loan.Builder toEntity(LoanDto dto);
}
