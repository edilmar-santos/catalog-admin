package com.eddy.admin.catalog.application.category.retrieve.list;

import com.eddy.admin.catalog.application.UseCase;
import com.eddy.admin.catalog.domain.category.CategorySearchQuery;
import com.eddy.admin.catalog.domain.pagination.Pagination;

public abstract class ListCategoryUseCase extends UseCase<CategorySearchQuery, Pagination<CategoryListOutput>> {
}
