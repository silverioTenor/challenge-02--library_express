package org.libraryexpress.application.loan.dto.request;

import org.libraryexpress.domain.enums.LoanStatus;

public record FilterLoansDto(
        String customerId,
        String ISBN,
        LoanStatus status
) {
}
