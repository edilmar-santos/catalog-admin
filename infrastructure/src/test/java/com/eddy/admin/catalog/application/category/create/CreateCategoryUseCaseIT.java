package com.eddy.admin.catalog.application.category.create;

import com.eddy.admin.catalog.IntegrationTest;
import com.eddy.admin.catalog.infrastructure.category.persistence.CategoryRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

@IntegrationTest
public class CreateCategoryUseCaseIT {

    @Autowired
    private CreateCategoryUseCase useCase;

    @Autowired
    private CategoryRepository repository;

    @Test
    void shouldCreateACategorySuccessfullyWhenExecuteIsCalled() {
        final var expectedName = "Category Name";
        final var expectedDescription = "Category Description";
        final var expectedIsActive = true;

        Assertions.assertEquals(0, repository.count());

        final var createCategoryCommand = CreateCategoryCommand.with(expectedName, expectedDescription, expectedIsActive);
        final var actualOutput = useCase.execute(createCategoryCommand).get();

        Assertions.assertNotNull(actualOutput);
        Assertions.assertNotNull(actualOutput.id());

        final var actualCategory = repository.findById(actualOutput.id()).get();

        Assertions.assertEquals(1, repository.count());
        Assertions.assertEquals(expectedName, actualCategory.getName());
        Assertions.assertEquals(expectedDescription, actualCategory.getDescription());
        Assertions.assertEquals(expectedIsActive, actualCategory.isActive());
        Assertions.assertNotNull(actualCategory.getCreatedAt());
        Assertions.assertNotNull(actualCategory.getUpdatedAt());
        Assertions.assertNull(actualCategory.getDeletedAt());
    }

}
