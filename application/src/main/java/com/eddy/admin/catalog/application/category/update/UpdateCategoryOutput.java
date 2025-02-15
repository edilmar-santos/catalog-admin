package com.eddy.admin.catalog.application.category.update;

import com.eddy.admin.catalog.domain.category.Category;
import com.eddy.admin.catalog.domain.category.CategoryID;

public record UpdateCategoryOutput(
        CategoryID id
) {
    public static UpdateCategoryOutput from(Category category) {
        return new UpdateCategoryOutput(category.getId());
    }
}
