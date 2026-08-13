package org.libraryexpress.application.loan.usecase;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.libraryexpress.application.UnitTest;
import org.libraryexpress.application.loan.dto.request.FilterLoansDto;
import org.libraryexpress.application.loan.dto.response.LoanDto;
import org.libraryexpress.application.loan.mapper.LoanMapper;
import org.libraryexpress.application.loan.validator.SearchLoanValidator;
import org.libraryexpress.domain.loan.entity.Loan;
import org.libraryexpress.domain.loan.enums.LoanStatus;
import org.libraryexpress.domain.loan.repository.LoanRepository;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@UnitTest
@ExtendWith(MockitoExtension.class)
@DisplayName("SearchLoans UseCase - Unit Test")
class SearchLoansTest {

    @Mock
    private LoanRepository loanRepository;

    @Mock
    private LoanMapper mapper;

    // Using @Spy for the stateless validator to execute its real production validation logic
    @Spy
    private SearchLoanValidator searchLoanValidator;

    @InjectMocks
    private SearchLoans searchLoans;

    private static final String CUSTOMER_ID = "customer-uuid-123";
    private static final String ISBN_STR = "978-43-12345-67-8";

    @Test
    @DisplayName("Should successfully return mapped DTOs when valid criteria are provided")
    void shouldReturnLoanDtos_whenFilterCriteriaAreValid() {
        // Arrange
        Set<LoanStatus> statuses = Set.of(LoanStatus.ACTIVE);
        FilterLoansDto validFilter = new FilterLoansDto(CUSTOMER_ID, ISBN_STR, statuses);

        Loan mockLoan = new Loan.Builder()
                .setId("loan-1")
                .setISBN(ISBN_STR)
                .setCustomerId(CUSTOMER_ID)
                .setStatus(LoanStatus.ACTIVE)
                .setStartDate(LocalDate.now())
                .build();

        LoanDto expectedDto = new LoanDto("loan-1", CUSTOMER_ID, ISBN_STR, LoanStatus.ACTIVE, LocalDate.now(), LocalDate.now().plusDays(15));

        // Stubbing repository query and mapper conversion layers
        when(loanRepository.search(CUSTOMER_ID, ISBN_STR, statuses)).thenReturn(Set.of(mockLoan));
        when(mapper.toResponseDto(mockLoan)).thenReturn(expectedDto);

        // Act
        Set<LoanDto> results = searchLoans.execute(validFilter);

        // Assert
        assertNotNull(results);
        assertEquals(1, results.size(), "Result set should contain exactly 1 mapped DTO");
        assertTrue(results.contains(expectedDto));

        // Style guide compliance: verify only critical business query delegations
        verify(loanRepository).search(CUSTOMER_ID, ISBN_STR, statuses);
    }

    @Test
    @DisplayName("Should throw IllegalArgumentException and bypass repository queries when filter has no criteria")
    void shouldThrowIllegalArgumentException_whenFilterContainsNoCriteria() {
        // Arrange
        // Fully empty criteria filter which triggers the validator block
        FilterLoansDto emptyFilter = new FilterLoansDto(null, null, null);

        // Act & Assert
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () ->
                searchLoans.execute(emptyFilter)
        );

        assertEquals("At least one search criteria must be provided", exception.getMessage());

        // Orchestration guard: ensure no repository resources or mappers are wasted when input validation fails
        verifyNoInteractions(loanRepository);
        verifyNoInteractions(mapper);
    }
}
