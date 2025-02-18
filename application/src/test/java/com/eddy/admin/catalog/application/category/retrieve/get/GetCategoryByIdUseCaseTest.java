package com.eddy.admin.catalog.application.category.retrieve.get;

import com.eddy.admin.catalog.domain.category.Category;
import com.eddy.admin.catalog.domain.category.CategoryGateway;
import com.eddy.admin.catalog.domain.category.CategoryID;
import com.eddy.admin.catalog.domain.exceptions.NotFoundException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class GetCategoryByIdUseCaseTest {

    @Mock
    private CategoryGateway categoryGateway;

    @InjectMocks
    private DefaultGetCategoryByIdUseCase getCategoryIdUseCase;

    @Test
    void shouldRetrieveAValidCategorySuccessfullyWhenCallExecute() {
        final var expectedName = "Category Name";
        final var expectedDescription = "Category Description";
        final var expectedIsActive = true;

        final var aCategory = Category.newCategory(expectedName, expectedDescription, expectedIsActive);
        final var expectedId = aCategory.getId();

        when(categoryGateway.getById(expectedId)).thenReturn(Optional.of(aCategory.clone()));

        final var actualCategory = getCategoryIdUseCase.execute(expectedId.getValue());

        assertNotNull(actualCategory);
        Assertions.assertEquals(expectedName, actualCategory.name());
        Assertions.assertEquals(expectedDescription, actualCategory.description());
        Assertions.assertEquals(expectedIsActive, actualCategory.active());
        Assertions.assertEquals(aCategory.getId(), actualCategory.id());
        Assertions.assertEquals(aCategory.getCreatedAt(), actualCategory.createdAt());
        Assertions.assertEquals(aCategory.getUpdatedAt(), actualCategory.updatedAt());
        Assertions.assertNull(actualCategory.deletedAt());

        Mockito.verify(categoryGateway, times(1)).getById(expectedId);
    }

    @Test
    void shouldReturnANotFoundExceptionWhenCategoryIdIsInvalid() {
        final var expectedId = CategoryID.from("invalidId");
        final var expectedErrorMessage = "Category ID %s not found.".formatted(expectedId.getValue());

        when(categoryGateway.getById(expectedId)).thenReturn(Optional.empty());

        final var actualException = Assertions.assertThrows(NotFoundException.class,
                () -> getCategoryIdUseCase.execute(expectedId.getValue()));

        Assertions.assertEquals(expectedErrorMessage, actualException.getMessage());

        Mockito.verify(categoryGateway, times(1)).getById(expectedId);
    }

    @Test
    void shouldReturnAGenericExceptionWhenCategoryGetByIdThrowsAnError() {
        final var expectedId = CategoryID.from("invalidId");
        final var expectedErrorMessage = "Error Generic";

        when(categoryGateway.getById(Mockito.any())).thenThrow(new IllegalArgumentException(expectedErrorMessage));

        final var actualException = Assertions.assertThrows(IllegalArgumentException.class,
                () -> getCategoryIdUseCase.execute(expectedId.getValue()));

        Assertions.assertEquals(expectedErrorMessage, actualException.getMessage());

        Mockito.verify(categoryGateway, times(1)).getById(expectedId);
    }
}
