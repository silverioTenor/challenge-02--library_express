package org.libraryexpress.application.loan.dto.response;

import org.libraryexpress.domain.enums.LoanStatus;

public record LoanDto(
        String customerId,
        String ISBN,
        LoanStatus status
) {
}
