package org.libraryexpress.infrastructure.api.controller;

import org.libraryexpress.application.loan.dto.request.CreateLoanDto;
import org.libraryexpress.application.loan.dto.request.FilterLoansDto;
import org.libraryexpress.application.loan.dto.response.LoanDto;
import org.libraryexpress.domain.core.dto.InputPaginationDto;
import org.libraryexpress.domain.core.dto.OutputPaginationDto;
import org.libraryexpress.domain.loan.enums.LoanStatus;
import org.libraryexpress.infrastructure.api.contract.Pagination;
import org.libraryexpress.infrastructure.api.routing.HttpContextRequest;
import org.libraryexpress.infrastructure.api.routing.HttpContextResponse;
import org.libraryexpress.infrastructure.config.AppContext;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

import static org.libraryexpress.infrastructure.api.enums.HttpStatusCode.*;

public final class LoanController {

    private final AppContext context;

    public LoanController(AppContext context) {
        this.context = context;
    }

    public void create(HttpContextRequest request, HttpContextResponse response) throws Exception {
        var inputDto = request.parseBody(CreateLoanDto.class);
        context.getCreateLoan().execute(inputDto);

        response.status(CREATED).sendEmpty();
    }

    public void search(HttpContextRequest request, HttpContextResponse response) throws Exception {
        Pagination.PageRequest pageRequest = request.getPageRequest();

        var inputDto = new FilterLoansDto(
                request.getQueryParam("customerId"),
                request.getQueryParam("ISBN"),
                request.getQueryParam("statuses"),
                pageRequest.page(),
                pageRequest.size()
        );
        OutputPaginationDto<LoanDto> outputDto = context.getSearchLoans().execute(inputDto);

        response.status(SUCCESS).json(outputDto);
    }

    public void returnLoan(HttpContextRequest request, HttpContextResponse response) throws Exception {
        String loanId = request.getRouteParam("loanId");
        context.getReturnLoan().execute(loanId);

        response.status(NO_CONTENT).sendEmpty();
    }

    /**
     * TODO - Temporary/palliative flow.
     * Once fine calculation and score adjustment are implemented directly in ReturnLoan,
     * this flow should become obsolete — OVERDUE loans should transition to FINISHED
     * automatically at return time, not via manual intervention.
     */
    public void closeOverdueLoan(HttpContextRequest request, HttpContextResponse response) throws Exception {
        String loanId = request.getRouteParam("loanId");
        context.getCloseOverdueLoan().execute(loanId);

        response.status(NO_CONTENT).sendEmpty();
    }
}
