package com.eddy.admin.catalog.application.category.retrieve.get;

import com.eddy.admin.catalog.IntegrationTest;
import com.eddy.admin.catalog.domain.category.Category;
import com.eddy.admin.catalog.infrastructure.category.persistence.CategoryJpaEntity;
import com.eddy.admin.catalog.infrastructure.category.persistence.CategoryRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.temporal.ChronoUnit;

import static org.junit.jupiter.api.Assertions.assertNotNull;

@IntegrationTest
public class GetCategoryByIdUseCaseIT {

    @Autowired
    private GetCategoryByIdUseCase useCase;

    @Autowired
    private CategoryRepository repository;

    @Test
    void shouldRetrieveAValidCategorySuccessfullyWhenCallExecute() {

        final var expectedName = "Category Name";
        final var expectedDescription = "Category Description";
        final var expectedIsActive = true;

        final var aCategory = Category.newCategory(expectedName, expectedDescription, expectedIsActive);
        final var expectedId = aCategory.getId();

        repository.saveAndFlush(CategoryJpaEntity.from(aCategory));

        final var actualCategory = useCase.execute(expectedId.getValue());

        assertNotNull(actualCategory);
        Assertions.assertEquals(expectedName, actualCategory.name());
        Assertions.assertEquals(expectedDescription, actualCategory.description());
        Assertions.assertEquals(expectedIsActive, actualCategory.active());
        Assertions.assertEquals(aCategory.getId(), actualCategory.id());
        Assertions.assertEquals(aCategory.getCreatedAt().truncatedTo(ChronoUnit.MILLIS), actualCategory.createdAt().truncatedTo(ChronoUnit.MILLIS));
        Assertions.assertEquals(aCategory.getUpdatedAt().truncatedTo(ChronoUnit.MILLIS), actualCategory.updatedAt().truncatedTo(ChronoUnit.MILLIS));
        Assertions.assertNull(actualCategory.deletedAt());
    }
}
