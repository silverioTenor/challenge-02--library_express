package org.libraryexpress.infrastructure.repository;

import org.libraryexpress.domain.entity.Loan;
import org.libraryexpress.domain.repository.ILoanRepository;
import org.libraryexpress.domain.enums.LoanStatus;

import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public enum LoanRepository implements ILoanRepository {
    DB;

    private final Set<Loan> group = new HashSet<>();

    @Override
    public void create(Loan loan) {
        group.add(loan);
    }

    @Override
    public boolean update(Loan loanToUpdate) {
        return group.stream()
                .filter(loan -> loan.equals(loanToUpdate))
                .findFirst()
                .map(loan -> {
                    loan.changeStatus(loanToUpdate.status());
                    return true;
                })
                .orElse(false);
    }

    @Override
    public Set<Loan> search(String customerId, String ISBN, Set<LoanStatus> statuses) {

        Predicate<Loan> criteria = loan -> true;

        if (Objects.nonNull(customerId) && !customerId.isBlank()) {
            criteria = criteria.and(loan -> loan.getCustomerId().equals(customerId));
        }

        if (Objects.nonNull(ISBN) && !ISBN.isBlank()) {
            criteria = criteria.and(loan -> loan.getISBN().equals(ISBN));
        }

        if (Objects.nonNull(statuses)) {
            criteria = criteria.and(loan -> statuses.contains(loan.status()));
        }

        return group.stream()
                .filter(criteria)
                .collect(Collectors.toSet());
    }

    @Override
    public Set<Loan> all() {
        return group;
    }
}
