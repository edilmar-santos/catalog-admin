package com.eddy.admin.catalog.domain.category;

import com.eddy.admin.catalog.domain.pagination.Pagination;

import java.util.Optional;

public interface CategoryGateway {

    Category create(Category category);

    void deleteById(CategoryID id);

    Optional<Category> getById(CategoryID id);

    Category update(Category category);

    Pagination<Category> findAll(CategorySearchQuery query);
}
