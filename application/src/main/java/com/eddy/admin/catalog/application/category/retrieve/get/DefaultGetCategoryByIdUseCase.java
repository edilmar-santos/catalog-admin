package com.eddy.admin.catalog.application.category.retrieve.get;

import com.eddy.admin.catalog.domain.category.CategoryGateway;
import com.eddy.admin.catalog.domain.category.CategoryID;
import com.eddy.admin.catalog.domain.exceptions.DomainException;
import com.eddy.admin.catalog.domain.validation.Error;

import java.util.function.Supplier;

public class DefaultGetCategoryByIdUseCase extends GetCategoryByIdUseCase {

    private final CategoryGateway categoryGateway;

    public DefaultGetCategoryByIdUseCase(final CategoryGateway categoryGateway) {
        this.categoryGateway = categoryGateway;
    }


    @Override
    public CategoryOutput execute(String input) {

        final var categoryId = CategoryID.from(input);

        return categoryGateway.getById(categoryId)
                .map(CategoryOutput::from)
                .orElseThrow(categoryNotFound(categoryId));
    }

    private static Supplier<DomainException> categoryNotFound(final CategoryID categoryId) {
        return () -> DomainException.with(new Error("Category ID %s not found".formatted(categoryId.getValue())));
    }
}
