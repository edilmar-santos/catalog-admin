package com.eddy.admin.catalog.infrastructure.category.presenter;

import com.eddy.admin.catalog.application.category.retrieve.get.CategoryOutput;
import com.eddy.admin.catalog.infrastructure.category.models.CategoryApiOutput;

public interface CategoryApiPresenter {

    static CategoryApiOutput presenter(final CategoryOutput output) {

        return new CategoryApiOutput(
                output.id().getValue(),
                output.name(),
                output.description(),
                output.active(),
                output.createdAt(),
                output.updatedAt(),
                output.deletedAt()
        );
    }
}
