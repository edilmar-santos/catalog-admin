package com.eddy.admin.catalog.application.category.create;

import com.eddy.admin.catalog.domain.category.CategoryGateway;
import com.eddy.admin.catalog.domain.category.CategoryValidator;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Objects;

import static java.util.Objects.isNull;
import static java.util.Objects.nonNull;
import static org.mockito.AdditionalAnswers.returnsFirstArg;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CreateCategoryUseCaseTest {

    @Mock
    private CategoryGateway categoryGatewayMocked;

    @InjectMocks
    private DefaultCreateCategoryUseCase useCase;


    @Test
    void shouldCreateACategorySuccessfullyWhenExecuteIsCalled() {
        final var expectedName = "Category Name";
        final var expectedDescription = "Category Description";
        final var expectedIsActive = true;

        final var createCategoryCommand = CreateCategoryCommand.with(expectedName, expectedDescription, expectedIsActive);

        when(categoryGatewayMocked.create(any())).thenAnswer(returnsFirstArg());

        final var actualCategory = useCase.execute(createCategoryCommand).get();

        Assertions.assertNotNull(actualCategory);
        Assertions.assertNotNull(actualCategory.id());

        verify(categoryGatewayMocked, times(1))
                .create(Mockito.argThat(category -> Objects.equals(expectedName, category.getName())
                        && Objects.equals(expectedDescription, category.getDescription())
                        && Objects.equals(expectedIsActive, category.isActive())
                        && nonNull(category.getId())
                        && nonNull(category.getCreatedAt())
                        && nonNull(category.getUpdatedAt())
                        && isNull(category.getDeletedAt())));
    }

    @Test
    void shouldCreateACategoryInactiveSuccessfullyWhenExecuteIsCalled() {
        final var expectedName = "Category Name";
        final var expectedDescription = "Category Description";
        final var expectedIsActive = false;
        final var expectedCallCount = 1;

        final var createCategoryCommand = CreateCategoryCommand.with(expectedName, expectedDescription, expectedIsActive);

        when(categoryGatewayMocked.create(any())).thenAnswer(returnsFirstArg());
        final var actualCategory = useCase.execute(createCategoryCommand).get();

        Assertions.assertNotNull(actualCategory);
        Assertions.assertNotNull(actualCategory.id());

        verify(categoryGatewayMocked, times(expectedCallCount))
                .create(Mockito.argThat(category -> Objects.equals(expectedName, category.getName())
                        && Objects.equals(expectedDescription, category.getDescription())
                        && Objects.equals(expectedIsActive, category.isActive())
                        && nonNull(category.getId())
                        && nonNull(category.getCreatedAt())
                        && nonNull(category.getUpdatedAt())
                        && nonNull(category.getDeletedAt())));
    }

    @Test
    void shouldThrowDomainExceptionWhenCategoryIsInvalid() {
        final String expectedName = null;
        final var expectedDescription = "Category Description";
        final var expectedIsActive = true;
        final var expectedExceptionMessage = CategoryValidator.NAME_SHOULD_NOT_BE_NULL_OR_BLANK;
        final var expectedErrorCount = 1;

        final var createCategoryCommand = CreateCategoryCommand.with(expectedName, expectedDescription, expectedIsActive);

        final var notification = useCase.execute(createCategoryCommand).getLeft();

        Assertions.assertEquals(expectedErrorCount, notification.getErrors().size());
        Assertions.assertEquals(expectedExceptionMessage, notification.getErrors().getFirst().message());

        verify(categoryGatewayMocked, times(0)).create(any());
    }

    @Test
    void shouldReturnAGenericExceptionWhenCategoryCreateThrowsAnError() {
        final var expectedName = "Category Name";
        final var expectedDescription = "Category Description";
        final var expectedIsActive = true;
        final var expectedErrorMessage = "Error Generic";
        final var expectedErrorCount = 1;

        final var createCategoryCommand = CreateCategoryCommand.with(expectedName, expectedDescription, expectedIsActive);

        when(categoryGatewayMocked.create(any())).thenThrow(new IllegalArgumentException(expectedErrorMessage));

        final var notification = useCase.execute(createCategoryCommand).getLeft();

        Assertions.assertEquals(expectedErrorCount, notification.getErrors().size());
        Assertions.assertEquals(expectedErrorMessage, notification.getErrors().getFirst().message());

        verify(categoryGatewayMocked, times(1))
                .create(Mockito.argThat(category -> Objects.equals(expectedName, category.getName())
                        && Objects.equals(expectedDescription, category.getDescription())
                        && Objects.equals(expectedIsActive, category.isActive())
                        && nonNull(category.getId())
                        && nonNull(category.getCreatedAt())
                        && nonNull(category.getUpdatedAt())
                        && isNull(category.getDeletedAt())));
    }
}