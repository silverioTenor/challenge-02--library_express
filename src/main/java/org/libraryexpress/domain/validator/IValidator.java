package org.libraryexpress.domain.validator;

import org.libraryexpress.infrastructure.exception.NotFoundException;
import org.libraryexpress.infrastructure.exception.RuleViolationException;

public interface IValidator<T> {

    void validate(T input) throws RuleViolationException, NotFoundException;
}
