package com.eddy.admin.catalog.application.category.create;

import com.eddy.admin.catalog.application.UseCase;
import com.eddy.admin.catalog.domain.validation.handler.Notification;
import io.vavr.control.Either;

public abstract class CreateCategoryUseCase extends UseCase<CreateCategoryCommand, Either<Notification, CreateCategoryOutput>> {
}
