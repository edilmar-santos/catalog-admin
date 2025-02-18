package com.eddy.admin.catalog.infrastructure.api.controllers;

import com.eddy.admin.catalog.application.category.create.CreateCategoryCommand;
import com.eddy.admin.catalog.application.category.create.CreateCategoryOutput;
import com.eddy.admin.catalog.application.category.create.CreateCategoryUseCase;
import com.eddy.admin.catalog.application.category.retrieve.get.GetCategoryByIdUseCase;
import com.eddy.admin.catalog.domain.pagination.Pagination;
import com.eddy.admin.catalog.domain.validation.handler.Notification;
import com.eddy.admin.catalog.infrastructure.api.CategoryAPI;
import com.eddy.admin.catalog.infrastructure.category.models.CategoryApiOutput;
import com.eddy.admin.catalog.infrastructure.category.models.CreateCategoryApiInput;
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

    public CategoryController(
            final CreateCategoryUseCase createCategoryUseCase,
            final GetCategoryByIdUseCase getCategoryByIdUseCase) {

        this.createCategoryUseCase = Objects.requireNonNull(createCategoryUseCase);
        this.getCategoryByIdUseCase = Objects.requireNonNull(getCategoryByIdUseCase);
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
}
