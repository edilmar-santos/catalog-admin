package com.eddy.admin.catalog.application.category.update;


import com.eddy.admin.catalog.domain.category.Category;
import com.eddy.admin.catalog.domain.category.CategoryGateway;
import com.eddy.admin.catalog.domain.category.CategoryID;
import com.eddy.admin.catalog.domain.category.CategoryValidator;
import com.eddy.admin.catalog.domain.exceptions.DomainException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Objects;
import java.util.Optional;

import static java.util.Objects.isNull;
import static java.util.Objects.nonNull;
import static org.mockito.AdditionalAnswers.returnsFirstArg;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UpdateCategoryUseCaseTest {

    @Mock
    private CategoryGateway categoryGateway;

    @InjectMocks
    private DefaultUpdateCategoryUseCase useCase;

    @Test
    void shouldUpdateACategorySuccessfullyWhenCallExecute() {

        final var aCategory = Category.newCategory("Film", null, true);

        final var expectedId = aCategory.getId();
        final var expectedName = "Category Name";
        final var expectedDescription = "Category Description";
        final var expectedIsActive = true;

        final var updateCategoryCommand = UpdateCategoryCommand.with(expectedId.getValue(), expectedName, expectedDescription, expectedIsActive);

        Mockito.when(categoryGateway.getById(expectedId)).thenReturn(Optional.of(aCategory.clone()));
        Mockito.when(categoryGateway.update(any())).thenAnswer(returnsFirstArg());

        final var actualOutput = useCase.execute(updateCategoryCommand).get();

        Assertions.assertNotNull(actualOutput);
        Assertions.assertNotNull(actualOutput.id());

        Mockito.verify(categoryGateway, times(1)).getById((expectedId));

        Mockito.verify(categoryGateway, times(1)).update(argThat(
                aUpdatedCategory ->
                        Objects.equals(expectedName, aUpdatedCategory.getName())
                                && Objects.equals(expectedDescription, aUpdatedCategory.getDescription())
                                && Objects.equals(expectedIsActive, aUpdatedCategory.isActive())
                                && Objects.equals(expectedId, aUpdatedCategory.getId())
                                && Objects.equals(aCategory.getCreatedAt(), aUpdatedCategory.getCreatedAt())
                                && aCategory.getUpdatedAt().isBefore(aUpdatedCategory.getUpdatedAt())
                                && Objects.isNull(aUpdatedCategory.getDeletedAt())
        ));
    }

    @Test
    void shouldThrowDomainExceptionWhenCategoryIsInvalid() {

        final var aCategory = Category.newCategory("Film", null, true);

        final var expectedId = aCategory.getId();
        final String expectedName = null;
        final var expectedDescription = "Category Description";
        final var expectedIsActive = true;
        final var expectedErrorCount = 1;
        final var expectedExceptionMessage = CategoryValidator.NAME_SHOULD_NOT_BE_NULL_OR_BLANK;

        final var updateCategoryCommand = UpdateCategoryCommand.with(expectedId.getValue(), expectedName, expectedDescription, expectedIsActive);

        Mockito.when(categoryGateway.getById(expectedId)).thenReturn(Optional.of(aCategory.clone()));

        final var notification = useCase.execute(updateCategoryCommand).getLeft();

        Assertions.assertEquals(expectedErrorCount, notification.getErrors().size());
        Assertions.assertEquals(expectedExceptionMessage, notification.getErrors().getFirst().message());

        verify(categoryGateway, times(0)).create(any());
    }

    @Test
    void shouldUpdateAnInactiveCategorySuccessfullyWhenCallExecute() {

        final var aCategory = Category.newCategory("Film", null, true);

        final var expectedId = aCategory.getId();
        final var expectedName = "Category Name";
        final var expectedDescription = "Category Description";
        final var expectedIsActive = false;

        final var updateCategoryCommand = UpdateCategoryCommand.with(expectedId.getValue(), expectedName, expectedDescription, expectedIsActive);

        Mockito.when(categoryGateway.getById(expectedId)).thenReturn(Optional.of(aCategory.clone()));
        Mockito.when(categoryGateway.update(any())).thenAnswer(returnsFirstArg());

        Assertions.assertTrue(aCategory.isActive());
        Assertions.assertNull(aCategory.getDeletedAt());

        final var actualOutput = useCase.execute(updateCategoryCommand).get();

        Assertions.assertNotNull(actualOutput);
        Assertions.assertNotNull(actualOutput.id());

        Mockito.verify(categoryGateway, times(1)).getById((expectedId));

        Mockito.verify(categoryGateway, times(1)).update(argThat(
                aUpdatedCategory ->
                        Objects.equals(expectedName, aUpdatedCategory.getName())
                                && Objects.equals(expectedDescription, aUpdatedCategory.getDescription())
                                && Objects.equals(expectedIsActive, aUpdatedCategory.isActive())
                                && Objects.equals(expectedId, aUpdatedCategory.getId())
                                && Objects.equals(aCategory.getCreatedAt(), aUpdatedCategory.getCreatedAt())
                                && aCategory.getUpdatedAt().isBefore(aUpdatedCategory.getUpdatedAt())
                                && Objects.nonNull(aUpdatedCategory.getDeletedAt())
        ));
    }

    @Test
    void shouldReturnAGenericExceptionWhenCategoryUpdateThrowsAnError() {

        final var aCategory = Category.newCategory("Film", null, true);

        final var expectedName = "Category Name";
        final var expectedDescription = "Category Description";
        final var expectedIsActive = true;
        final var expectedErrorMessage = "Error Generic";
        final var expectedErrorCount = 1;
        final var expectedId = aCategory.getId();

        final var updateCategoryCommand = UpdateCategoryCommand.with(expectedId.getValue(), expectedName, expectedDescription, expectedIsActive);

        Mockito.when(categoryGateway.getById(expectedId)).thenReturn(Optional.of(aCategory.clone()));
        when(categoryGateway.update(any())).thenThrow(new IllegalArgumentException(expectedErrorMessage));

        final var notification = useCase.execute(updateCategoryCommand).getLeft();

        Assertions.assertEquals(expectedErrorCount, notification.getErrors().size());
        Assertions.assertEquals(expectedErrorMessage, notification.getErrors().getFirst().message());

        verify(categoryGateway, times(1))
                .update(Mockito.argThat(category -> Objects.equals(expectedName, category.getName())
                        && Objects.equals(expectedDescription, category.getDescription())
                        && Objects.equals(expectedIsActive, category.isActive())
                        && nonNull(category.getId())
                        && nonNull(category.getCreatedAt())
                        && nonNull(category.getUpdatedAt())
                        && isNull(category.getDeletedAt())));
    }

    @Test
    void shouldReturnANotFoundExceptionWhenCategoryIdIsInvalid() {

        final var expectedName = "Category Name";
        final var expectedDescription = "Category Description";
        final var expectedIsActive = true;
        final var invalidId = "Invalid Id";
        final var expectedErrorMessage = "Category ID %s not found.".formatted(invalidId);

        when(categoryGateway.getById(CategoryID.from(invalidId))).thenReturn(Optional.empty());

        final var actualException = Assertions.assertThrows(DomainException.class,
                () -> useCase.execute(UpdateCategoryCommand.with(invalidId, expectedName, expectedDescription, expectedIsActive)));

        Assertions.assertEquals(expectedErrorMessage, actualException.getMessage());

        verify(categoryGateway, times(1)).getById(CategoryID.from(invalidId));
        verify(categoryGateway, times(0)).update(any());
    }
}
