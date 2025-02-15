package com.eddy.admin.catalog.application.category.delete;

import com.eddy.admin.catalog.IntegrationTest;
import com.eddy.admin.catalog.domain.category.Category;
import com.eddy.admin.catalog.infrastructure.category.persistence.CategoryJpaEntity;
import com.eddy.admin.catalog.infrastructure.category.persistence.CategoryRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

@IntegrationTest
public class DeleteCategoryUseCaseIT {

    @Autowired
    private DeleteCategoryUseCase useCase;

    @Autowired
    private CategoryRepository repository;


    @Test
    void shouldDeleteACategorySuccessfullyWhenCategoryExists() {

        final var aCategory = Category.newCategory("Film", "Film Description", true);

        repository.saveAndFlush(CategoryJpaEntity.from(aCategory));

        Assertions.assertEquals(1, repository.count());

        useCase.execute(aCategory.getId().getValue());

        Assertions.assertEquals(0, repository.count());
    }
}
