package com.eddy.admin.catalog.infrastructure.api.controllers;

import com.eddy.admin.catalog.application.category.create.CreateCategoryCommand;
import com.eddy.admin.catalog.application.category.create.CreateCategoryOutput;
import com.eddy.admin.catalog.application.category.create.CreateCategoryUseCase;
import com.eddy.admin.catalog.application.category.delete.DeleteCategoryUseCase;
import com.eddy.admin.catalog.application.category.retrieve.get.GetCategoryByIdUseCase;
import com.eddy.admin.catalog.application.category.update.UpdateCategoryCommand;
import com.eddy.admin.catalog.application.category.update.UpdateCategoryOutput;
import com.eddy.admin.catalog.application.category.update.UpdateCategoryUseCase;
import com.eddy.admin.catalog.domain.pagination.Pagination;
import com.eddy.admin.catalog.domain.validation.handler.Notification;
import com.eddy.admin.catalog.infrastructure.api.CategoryAPI;
import com.eddy.admin.catalog.infrastructure.category.models.CategoryApiOutput;
import com.eddy.admin.catalog.infrastructure.category.models.CreateCategoryApiInput;
import com.eddy.admin.catalog.infrastructure.category.models.UpdateCategoryApiInput;
import com.eddy.admin.catalog.infrastructure.category.presenter.CategoryApiPresenter;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;
import java.util.Objects;
import java.util.function.Function;

@RestController
public class CategoryController implements CategoryAPI {

    private final CreateCategoryUseCase createCategoryUseCase;
    private final GetCategoryByIdUseCase getCategoryByIdUseCase;
    private final UpdateCategoryUseCase updateCategoryUseCase;
    private final DeleteCategoryUseCase deleteCategoryUseCase;

    public CategoryController(
            final CreateCategoryUseCase createCategoryUseCase,
            final GetCategoryByIdUseCase getCategoryByIdUseCase,
            final UpdateCategoryUseCase updateCategoryUseCase,
            final DeleteCategoryUseCase deleteCategoryUseCase) {

        this.createCategoryUseCase = Objects.requireNonNull(createCategoryUseCase);
        this.getCategoryByIdUseCase = Objects.requireNonNull(getCategoryByIdUseCase);
        this.updateCategoryUseCase = Objects.requireNonNull(updateCategoryUseCase);
        this.deleteCategoryUseCase = Objects.requireNonNull(deleteCategoryUseCase);
    }

    @Override
    public ResponseEntity<?> createCategory(final CreateCategoryApiInput input) {
        final var aCommand = CreateCategoryCommand.with(input.name(), input.description(), input.active());

        Function<Notification, ResponseEntity<?>> onError = notification ->
                ResponseEntity.unprocessableEntity().body(notification);

        Function<CreateCategoryOutput, ResponseEntity<?>> onSuccess = categoryOutput ->
                ResponseEntity.created(URI.create("/categories/" + categoryOutput.id())).body(categoryOutput);

        return createCategoryUseCase.execute(aCommand)
                .fold(onError, onSuccess);
    }

    @Override
    public Pagination<?> listCategories(String search, int page, int perPage, String sort) {
        return null;
    }

    @Override
    public CategoryApiOutput getCategoryById(final String id) {
        return CategoryApiPresenter.presenter(getCategoryByIdUseCase.execute(id));
    }

    @Override
    public ResponseEntity<?> updateCategory(final UpdateCategoryApiInput input, final String id) {
        final var aCommand = UpdateCategoryCommand.with(id, input.name(), input.description(), input.active());

        Function<Notification, ResponseEntity<?>> onError = notification ->
                ResponseEntity.unprocessableEntity().body(notification);

        Function<UpdateCategoryOutput, ResponseEntity<?>> onSuccess = ResponseEntity::ok;

        return updateCategoryUseCase.execute(aCommand)
                .fold(onError, onSuccess);
    }

    @Override
    public void deleteCategory(final String id) {
        deleteCategoryUseCase.execute(id);
    }
}
