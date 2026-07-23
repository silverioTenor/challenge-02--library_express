package org.libraryexpress.application.loan.dto.request;

import org.libraryexpress.domain.enums.LoanStatus;

import java.util.Set;

public record FilterLoansDto(
        String customerId,
        String ISBN,
        Set<LoanStatus> statuses
) {
}
