package com.eddy.admin.catalog.application.category.update;

import com.eddy.admin.catalog.IntegrationTest;
import com.eddy.admin.catalog.domain.category.Category;
import com.eddy.admin.catalog.infrastructure.category.persistence.CategoryJpaEntity;
import com.eddy.admin.catalog.infrastructure.category.persistence.CategoryRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.temporal.ChronoUnit;

@IntegrationTest
public class UpdateCategoryUseCaseIT {

    @Autowired
    private UpdateCategoryUseCase useCase;

    @Autowired
    private CategoryRepository repository;

    @Test
    void shouldUpdateACategorySuccessfullyWhenCallExecute() {

        Assertions.assertEquals(0, repository.count());
        final var aCategory = Category.newCategory("Film", null, true);

        repository.saveAndFlush(CategoryJpaEntity.from(aCategory));

        Assertions.assertEquals(1, repository.count());

        final var expectedId = aCategory.getId();
        final var expectedName = "Category Name";
        final var expectedDescription = "Category Description";
        final var expectedIsActive = true;

        final var updateCategoryCommand = UpdateCategoryCommand.with(expectedId.getValue(), expectedName, expectedDescription, expectedIsActive);
        final var actualOutput = useCase.execute(updateCategoryCommand).get();

        Assertions.assertEquals(1, repository.count());

        Assertions.assertNotNull(actualOutput);
        Assertions.assertNotNull(actualOutput.id());

        final var actualCategory = repository.findById(actualOutput.id().getValue()).get();

        Assertions.assertEquals(expectedId.getValue(), actualCategory.getId());
        Assertions.assertEquals(expectedName, actualCategory.getName());
        Assertions.assertEquals(expectedDescription, actualCategory.getDescription());
        Assertions.assertEquals(expectedIsActive, actualCategory.isActive());
        Assertions.assertEquals(aCategory.getCreatedAt().truncatedTo(ChronoUnit.MILLIS), actualCategory.getCreatedAt().truncatedTo(ChronoUnit.MILLIS));
        Assertions.assertTrue(aCategory.getUpdatedAt().isBefore(actualCategory.getUpdatedAt()));
        Assertions.assertNull(actualCategory.getDeletedAt());
    }
}
