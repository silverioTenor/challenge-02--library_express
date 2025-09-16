package org.libraryexpress.infrastructure.repository;

import org.libraryexpress.domain.entity.Loan;
import org.libraryexpress.domain.repository.ILoanRepository;
import org.libraryexpress.domain.enums.LoanStatus;

import java.util.*;
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
    public Set<Loan> getByStatus(LoanStatus status) {
        return group.stream()
                .filter(loan -> loan.status().equals(status))
                .collect(Collectors.toSet());
    }

    @Override
    public Set<Loan> getByClientId(String clientId) {
        return group.stream()
                .filter(loan -> loan.getClient().getID().equals(clientId))
                .collect(Collectors.toSet());
    }

    @Override
    public Set<Loan> getByBookIsbn(String isbn) {
        return group.stream()
                .filter(loan -> loan.getBook().ISBN.equals(isbn))
                .collect(Collectors.toSet());
    }

    @Override
    public Set<Loan> all() {
        return group;
    }
}
