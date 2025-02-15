package com.eddy.admin.catalog.domain.validation;

import java.util.List;

import static java.util.Objects.nonNull;

public interface ValidationHandler {

    ValidationHandler append(Error error);

    ValidationHandler append(ValidationHandler handler);

    ValidationHandler validate(Validation validation);

    List<Error> getErrors();

    default boolean hasErrors() {

        return nonNull(getErrors()) && !getErrors().isEmpty();
    }

    default Error firstError() {

        return hasErrors() ? getErrors().get(0) : null;
    }

    interface Validation {
        void validate();
    }
}
