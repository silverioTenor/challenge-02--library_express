package org.libraryexpress.application.loan.dto.response;

import org.libraryexpress.domain.enums.LoanStatus;

import java.time.LocalDate;

public record LoanDto(
        String id,
        String customerId,
        String ISBN,
        LoanStatus status,
        LocalDate startDate,
        LocalDate endDate
) {
}
