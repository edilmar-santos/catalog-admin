package com.eddy.admin.catalog.application.category.update;

import com.eddy.admin.catalog.application.UseCase;
import com.eddy.admin.catalog.domain.validation.handler.Notification;
import io.vavr.control.Either;

public abstract class UpdateCategoryUseCase extends UseCase<UpdateCategoryCommand, Either<Notification, UpdateCategoryOutput>> {
}
