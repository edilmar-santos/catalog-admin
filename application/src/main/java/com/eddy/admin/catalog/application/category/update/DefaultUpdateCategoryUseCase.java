package com.eddy.admin.catalog.application.category.update;

import com.eddy.admin.catalog.domain.category.Category;
import com.eddy.admin.catalog.domain.category.CategoryGateway;
import com.eddy.admin.catalog.domain.category.CategoryID;
import com.eddy.admin.catalog.domain.exceptions.DomainException;
import com.eddy.admin.catalog.domain.exceptions.NotFoundException;
import com.eddy.admin.catalog.domain.validation.handler.Notification;
import io.vavr.API;
import io.vavr.control.Either;

import java.util.function.Supplier;

public class DefaultUpdateCategoryUseCase extends UpdateCategoryUseCase {

    private final CategoryGateway categoryGateway;

    public DefaultUpdateCategoryUseCase(CategoryGateway categoryGateway) {
        this.categoryGateway = categoryGateway;
    }


    @Override
    public Either<Notification, UpdateCategoryOutput> execute(final UpdateCategoryCommand categoryCommand) {

        final var categoryID = CategoryID.from(categoryCommand.id());
        final var aCategory = categoryGateway.getById(categoryID)
                .orElseThrow(categoryNotFound(categoryCommand));

        final var notification = Notification.create();
        aCategory.update(categoryCommand.name(), categoryCommand.description(), categoryCommand.isActive())
                .validate(notification);

        return notification.hasErrors() ? API.Left(notification) : update(aCategory);
    }

    private static Supplier<DomainException> categoryNotFound(final UpdateCategoryCommand categoryCommand) {
        return () -> NotFoundException.with(Category.class, CategoryID.from(categoryCommand.id()));
    }

    private Either<Notification, UpdateCategoryOutput> update(final Category category) {
        return API.Try(() -> this.categoryGateway.update(category))
                .toEither()
                .bimap(Notification::create, UpdateCategoryOutput::from);
    }
}
