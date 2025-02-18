package com.eddy.admin.catalog.domain.exceptions;

import com.eddy.admin.catalog.domain.AggregateRoot;
import com.eddy.admin.catalog.domain.Identifier;
import com.eddy.admin.catalog.domain.validation.Error;

import java.util.List;

public class NotFoundException extends DomainException {

    private NotFoundException(final String message, final List<Error> errors) {
        super(message, errors);
    }

    public static NotFoundException with(
            final Class<? extends AggregateRoot<?>> anAggregateRootClass,
            final Identifier id
    ) {
        final var errorMessage = "%s ID %s not found."
                .formatted(anAggregateRootClass.getSimpleName(), id.getValue());
        return new NotFoundException(errorMessage, List.of());
    }
}
