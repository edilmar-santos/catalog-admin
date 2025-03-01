package com.eddy.admin.catalog.infrastructure.category.presenter;

import com.eddy.admin.catalog.application.category.retrieve.get.CategoryOutput;
import com.eddy.admin.catalog.application.category.retrieve.list.CategoryListOutput;
import com.eddy.admin.catalog.infrastructure.category.models.CategoryResponse;
import com.eddy.admin.catalog.infrastructure.category.models.ListCategoryResponse;

public interface CategoryApiPresenter {

    static CategoryResponse presenter(final CategoryOutput output) {

        return new CategoryResponse(
                output.id().getValue(),
                output.name(),
                output.description(),
                output.active(),
                output.createdAt(),
                output.updatedAt(),
                output.deletedAt()
        );
    }

    static ListCategoryResponse presenter(final CategoryListOutput output) {

        return new ListCategoryResponse(
                output.id().getValue(),
                output.name(),
                output.description(),
                output.active(),
                output.createdAt(),
                output.deletedAt()
        );
    }
}
