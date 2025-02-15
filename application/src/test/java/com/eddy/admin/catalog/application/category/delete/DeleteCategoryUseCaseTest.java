package com.eddy.admin.catalog.application.category.delete;

import com.eddy.admin.catalog.domain.category.Category;
import com.eddy.admin.catalog.domain.category.CategoryGateway;
import com.eddy.admin.catalog.domain.category.CategoryID;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;

@ExtendWith(MockitoExtension.class)
public class DeleteCategoryUseCaseTest {

    @InjectMocks
    private DefaultDeleteCategoryUseCase deleteCategoryUseCase;

    @Mock
    private CategoryGateway categoryGateway;


    @Test
    void shouldDeleteACategorySuccessfullyWhenCategoryExists() {

        final var aCategory = Category.newCategory("Film", "Film Description", true);
        final var expectedId = aCategory.getId();

        doNothing().when(categoryGateway).deleteById(eq(expectedId));

        Assertions.assertDoesNotThrow(() -> deleteCategoryUseCase.execute(expectedId.getValue()));

        Mockito.verify(categoryGateway, Mockito.times(1)).deleteById(eq(expectedId));
    }

    @Test
    void shouldNotThrowErrorWhenCategoryDoesNotExists() {

        final var expectedId = CategoryID.from("invalidId");

        doNothing().when(categoryGateway).deleteById(eq(expectedId));

        Assertions.assertDoesNotThrow(() -> deleteCategoryUseCase.execute(expectedId.getValue()));

        Mockito.verify(categoryGateway, Mockito.times(1)).deleteById(eq(expectedId));
    }

    @Test
    void shouldThrowsExceptionWhenCategoryGatewayReturnError() {

        final var aCategory = Category.newCategory("Film", "Film Description", true);
        final var expectedId = aCategory.getId();
        final var expectedErrorMessage = "Gatewar Error";

        Mockito.doThrow(new IllegalArgumentException(expectedErrorMessage)).when(categoryGateway).deleteById(eq(expectedId));

        Assertions.assertThrows(IllegalArgumentException.class, () -> deleteCategoryUseCase.execute(expectedId.getValue()));

        Mockito.verify(categoryGateway, Mockito.times(1)).deleteById(eq(expectedId));
    }
}
