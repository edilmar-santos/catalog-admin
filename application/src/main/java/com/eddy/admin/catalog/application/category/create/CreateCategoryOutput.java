package com.eddy.admin.catalog.application.category.create;

import com.eddy.admin.catalog.domain.category.Category;

public record CreateCategoryOutput(
        String id
) {

    public static CreateCategoryOutput from(final Category category) {
        return new CreateCategoryOutput(category.getId().getValue());
    }

    public static CreateCategoryOutput from(final String categoryId) {
        return new CreateCategoryOutput(categoryId);
    }
}
