package com.eddy.admin.catalog.infrastructure.api.controllers;

import com.eddy.admin.catalog.application.category.create.CreateCategoryCommand;
import com.eddy.admin.catalog.application.category.create.CreateCategoryOutput;
import com.eddy.admin.catalog.application.category.create.CreateCategoryUseCase;
import com.eddy.admin.catalog.application.category.delete.DeleteCategoryUseCase;
import com.eddy.admin.catalog.application.category.retrieve.get.GetCategoryByIdUseCase;
import com.eddy.admin.catalog.application.category.retrieve.list.ListCategoryUseCase;
import com.eddy.admin.catalog.application.category.update.UpdateCategoryCommand;
import com.eddy.admin.catalog.application.category.update.UpdateCategoryOutput;
import com.eddy.admin.catalog.application.category.update.UpdateCategoryUseCase;
import com.eddy.admin.catalog.domain.category.CategorySearchQuery;
import com.eddy.admin.catalog.domain.pagination.Pagination;
import com.eddy.admin.catalog.domain.validation.handler.Notification;
import com.eddy.admin.catalog.infrastructure.api.CategoryAPI;
import com.eddy.admin.catalog.infrastructure.category.models.CategoryResponse;
import com.eddy.admin.catalog.infrastructure.category.models.CreateCategoryRequest;
import com.eddy.admin.catalog.infrastructure.category.models.ListCategoryResponse;
import com.eddy.admin.catalog.infrastructure.category.models.UpdateCategoryRequest;
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
    private final ListCategoryUseCase listCategoryUseCase;

    public CategoryController(
            final CreateCategoryUseCase createCategoryUseCase,
            final GetCategoryByIdUseCase getCategoryByIdUseCase,
            final UpdateCategoryUseCase updateCategoryUseCase,
            final DeleteCategoryUseCase deleteCategoryUseCase,
            final ListCategoryUseCase listCategoryUseCase) {

        this.createCategoryUseCase = Objects.requireNonNull(createCategoryUseCase);
        this.getCategoryByIdUseCase = Objects.requireNonNull(getCategoryByIdUseCase);
        this.updateCategoryUseCase = Objects.requireNonNull(updateCategoryUseCase);
        this.deleteCategoryUseCase = Objects.requireNonNull(deleteCategoryUseCase);
        this.listCategoryUseCase = Objects.requireNonNull(listCategoryUseCase);
    }

    @Override
    public ResponseEntity<?> createCategory(final CreateCategoryRequest input) {
        final var aCommand = CreateCategoryCommand.with(input.name(), input.description(), input.active());

        Function<Notification, ResponseEntity<?>> onError = notification ->
                ResponseEntity.unprocessableEntity().body(notification);

        Function<CreateCategoryOutput, ResponseEntity<?>> onSuccess = categoryOutput ->
                ResponseEntity.created(URI.create("/categories/" + categoryOutput.id())).body(categoryOutput);

        return createCategoryUseCase.execute(aCommand)
                .fold(onError, onSuccess);
    }

    @Override
    public Pagination<ListCategoryResponse> listCategories(
            final String search,
            final int page,
            final int perPage,
            final String sort,
            final String direction) {
        final var aQuery = new CategorySearchQuery(page, perPage, search, sort, direction);

        return listCategoryUseCase.execute(aQuery)
                    .map(CategoryApiPresenter::presenter);
    }

    @Override
    public CategoryResponse getCategoryById(final String id) {
        return CategoryApiPresenter.presenter(getCategoryByIdUseCase.execute(id));
    }

    @Override
    public ResponseEntity<?> updateCategory(final UpdateCategoryRequest input, final String id) {
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
