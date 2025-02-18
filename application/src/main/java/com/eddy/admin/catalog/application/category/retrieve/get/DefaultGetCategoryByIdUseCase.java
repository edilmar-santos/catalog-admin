package com.eddy.admin.catalog.application.category.retrieve.get;

import com.eddy.admin.catalog.domain.category.Category;
import com.eddy.admin.catalog.domain.category.CategoryGateway;
import com.eddy.admin.catalog.domain.category.CategoryID;
import com.eddy.admin.catalog.domain.exceptions.NotFoundException;

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

    private static Supplier<NotFoundException> categoryNotFound(final CategoryID categoryId) {
        return () -> NotFoundException.with(Category.class, categoryId);
    }
}
