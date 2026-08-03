package org.libraryexpress.domain.validator;

import org.libraryexpress.domain.exception.NotFoundException;
import org.libraryexpress.domain.exception.RuleViolationException;

public interface Validator<T> {

    void validate(T input) throws Exception;
}
