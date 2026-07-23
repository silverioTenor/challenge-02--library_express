package org.libraryexpress.application.loan.validator;

import org.libraryexpress.application.loan.dto.request.FilterLoansDto;
import org.libraryexpress.infrastructure.exception.RuleViolationException;

public class SearchLoanValidator {

    public void validate(FilterLoansDto filter) throws RuleViolationException {

        boolean hasAnyCriteria = filter.statuses() != null
                || (filter.customerId() != null && !filter.customerId().isBlank())
                || (filter.ISBN() != null && !filter.ISBN().isBlank());

        if (!hasAnyCriteria) throw new RuleViolationException("At least one search criteria must be provided");
    }
}
