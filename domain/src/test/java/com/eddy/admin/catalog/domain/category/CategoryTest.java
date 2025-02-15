package com.eddy.admin.catalog.domain.category;

import com.eddy.admin.catalog.domain.exceptions.DomainException;
import com.eddy.admin.catalog.domain.validation.handler.ThrowsValidationHandler;
import org.junit.jupiter.api.Test;

import static com.eddy.admin.catalog.domain.category.CategoryValidator.NAME_MUST_BE_BETWEEN_3_AND_255_CHARACTERS;
import static com.eddy.admin.catalog.domain.category.CategoryValidator.NAME_SHOULD_NOT_BE_NULL_OR_BLANK;
import static org.junit.jupiter.api.Assertions.*;

class CategoryTest {

    @Test
    void shouldCreateACategorySuccessfullyWhenAllDataIsCorrectly() {
        String id = "1";
        String name = "Category";
        String description = "Category description";
        boolean active = true;

        Category category = Category.newCategory(name, description, active);

        assertNotNull(category);
        assertEquals(name, category.getName());
        assertEquals(description, category.getDescription());
        assertEquals(active, category.isActive());
        assertNotNull(category.getCreatedAt());
        assertNotNull(id);
        assertNotNull(category.getUpdatedAt());
        assertNull(category.getDeletedAt());
    }

    @Test
    void shouldThrowExceptionWhenIsCreatingACategoryWithNullName() {
        final String expectedName = null;
        final var expectedCount = 1;
        final var expectedDescription = "Category description";
        final var expectedIsActive = true;

        final var actualCategory = Category.newCategory(expectedName, expectedDescription, expectedIsActive);

        final var actualException = assertThrows(DomainException.class, () -> actualCategory.validate(new ThrowsValidationHandler()));

        assertEquals(NAME_SHOULD_NOT_BE_NULL_OR_BLANK, actualException.getErrors().getFirst().message());
        assertEquals(expectedCount, actualException.getErrors().size());
    }

    @Test
    void shouldThrowExceptionWhenIsCreatingACategoryWithEmptyName() {
        final String expectedName = "";
        final var expectedCount = 1;
        final var expectedDescription = "Category description";
        final var expectedIsActive = true;

        final var actualCategory = Category.newCategory(expectedName, expectedDescription, expectedIsActive);

        final var actualException = assertThrows(DomainException.class, () -> actualCategory.validate(new ThrowsValidationHandler()));

        assertEquals(NAME_SHOULD_NOT_BE_NULL_OR_BLANK, actualException.getErrors().getFirst().message());
        assertEquals(expectedCount, actualException.getErrors().size());
    }

    @Test
    void shouldThrowExceptionWhenIsCreatingACategoryWithNameSizeLessThan3() {
        final String expectedName = "ab ";
        final var expectedCount = 1;
        final var expectedDescription = "Category description";
        final var expectedIsActive = true;

        final var actualCategory = Category.newCategory(expectedName, expectedDescription, expectedIsActive);

        final var actualException = assertThrows(DomainException.class, () -> actualCategory.validate(new ThrowsValidationHandler()));

        assertEquals(NAME_MUST_BE_BETWEEN_3_AND_255_CHARACTERS, actualException.getErrors().getFirst().message());
        assertEquals(expectedCount, actualException.getErrors().size());
    }

    @Test
    void shouldThrowExceptionWhenIsCreatingACategoryWithNameSizeGreaterThan255() {
        final String expectedName = "a".repeat(256);
        final var expectedCount = 1;
        final var expectedDescription = "Category description";
        final var expectedIsActive = true;

        final var actualCategory = Category.newCategory(expectedName, expectedDescription, expectedIsActive);

        final var actualException = assertThrows(DomainException.class, () -> actualCategory.validate(new ThrowsValidationHandler()));

        assertEquals(NAME_MUST_BE_BETWEEN_3_AND_255_CHARACTERS, actualException.getErrors().getFirst().message());
        assertEquals(expectedCount, actualException.getErrors().size());
    }

    @Test
    void shouldNotThrowExceptionWhenIsCreatingACategoryWithEmptyDescription() {
        final var expectedName = "Category";
        final var expectedDescription = "";
        final var expectedIsActive = true;

        final var actualCategory = Category.newCategory(expectedName, expectedDescription, expectedIsActive);

        assertDoesNotThrow(() -> actualCategory.validate(new ThrowsValidationHandler()));

        assertNotNull(actualCategory);
        assertEquals(expectedName, actualCategory.getName());
        assertEquals(expectedDescription, actualCategory.getDescription());
        assertEquals(expectedIsActive, actualCategory.isActive());
        assertNotNull(actualCategory.getCreatedAt());
        assertNotNull(actualCategory.getId());
        assertNotNull(actualCategory.getUpdatedAt());
        assertNull(actualCategory.getDeletedAt());
    }

    @Test
    void shouldCreateACategorySuccessfullyWhenAllDataIsCorrectlyAndActiveIsFalse() {
        final var expectedName = "Category";
        final var expectedDescription = "Category description";
        final var expectedIsActive = false;

        final var actualCategory = Category.newCategory(expectedName, expectedDescription, expectedIsActive);

        assertDoesNotThrow(() -> actualCategory.validate(new ThrowsValidationHandler()));
        assertNotNull(actualCategory);
        assertEquals(expectedName, actualCategory.getName());
        assertEquals(expectedDescription, actualCategory.getDescription());
        assertEquals(expectedIsActive, actualCategory.isActive());
        assertNotNull(actualCategory.getCreatedAt());
        assertNotNull(actualCategory.getId());
        assertNotNull(actualCategory.getUpdatedAt());
        assertNotNull(actualCategory.getDeletedAt());
    }

    @Test
    void shouldDeactivateCategorySuccessfullyWhenCategoryCallDeactivateMethod() {
        final var expectedName = "Category";
        final var expectedDescription = "Category description";
        final var expectedIsActive = false;

        final var aCategory = Category.newCategory(expectedName, expectedDescription, true);

        assertDoesNotThrow(() -> aCategory.validate(new ThrowsValidationHandler()));
        final var updatedAt = aCategory.getUpdatedAt();
        assertTrue(aCategory.isActive());
        assertNull(aCategory.getDeletedAt());

        final var actualCategory = aCategory.deactivate();

        assertDoesNotThrow(() -> aCategory.validate(new ThrowsValidationHandler()));
        assertNotNull(actualCategory.getId());
        assertEquals(expectedIsActive, actualCategory.isActive());
        assertEquals(expectedName, actualCategory.getName());
        assertEquals(expectedDescription, actualCategory.getDescription());
        assertEquals(actualCategory.getCreatedAt(), aCategory.getCreatedAt());
        assertEquals(actualCategory.getId(), aCategory.getId());
        assertTrue(actualCategory.getUpdatedAt().isAfter(updatedAt));
        assertNotNull(actualCategory.getDeletedAt());
    }

    @Test
    void shouldActivateCategorySuccessfullyWhenCategoryCallActivateMethod() {
        final var expectedName = "Category";
        final var expectedDescription = "Category description";
        final var expectedIsActive = true;

        final var aCategory = Category.newCategory(expectedName, expectedDescription, false);

        assertDoesNotThrow(() -> aCategory.validate(new ThrowsValidationHandler()));
        final var updatedAt = aCategory.getUpdatedAt();
        assertFalse(aCategory.isActive());
        assertNotNull(aCategory.getDeletedAt());

        final var actualCategory = aCategory.activate();

        assertDoesNotThrow(() -> aCategory.validate(new ThrowsValidationHandler()));
        assertNotNull(actualCategory.getId());
        assertEquals(expectedIsActive, actualCategory.isActive());
        assertEquals(expectedName, actualCategory.getName());
        assertEquals(expectedDescription, actualCategory.getDescription());
        assertEquals(actualCategory.getCreatedAt(), aCategory.getCreatedAt());
        assertEquals(actualCategory.getId(), aCategory.getId());
        assertTrue(actualCategory.getUpdatedAt().isAfter(updatedAt));
        assertNull(actualCategory.getDeletedAt());
    }

    @Test
    void shouldUpdateACategorySuccessfullyWhenCategoryCallUpdateMethod() {
        final var expectedName = "Category";
        final var expectedDescription = "Category description";
        final var expectedIsActive = true;

        final var aCategory = Category.newCategory("Movie", "Movie Description", expectedIsActive);

        assertDoesNotThrow(() -> aCategory.validate(new ThrowsValidationHandler()));

        final var updatedAt = aCategory.getUpdatedAt();
        assertTrue(aCategory.isActive());
        assertNull(aCategory.getDeletedAt());

        final var actualCategory = aCategory.update(expectedName, expectedDescription, expectedIsActive);

        assertDoesNotThrow(() -> aCategory.validate(new ThrowsValidationHandler()));
        assertNotNull(actualCategory.getId());
        assertEquals(expectedIsActive, actualCategory.isActive());
        assertEquals(expectedName, actualCategory.getName());
        assertEquals(expectedDescription, actualCategory.getDescription());
        assertEquals(actualCategory.getCreatedAt(), aCategory.getCreatedAt());
        assertEquals(actualCategory.getId(), aCategory.getId());
        assertTrue(actualCategory.getUpdatedAt().isAfter(updatedAt));
        assertNull(actualCategory.getDeletedAt());
    }

    @Test
    void shouldUpdateACategorySuccessfullyWhenCategoryCallUpdateMethodInactivating() {
        final var expectedName = "Category";
        final var expectedDescription = "Category description";
        final var expectedIsActive = false;

        final var aCategory = Category.newCategory("Movie", "Movie Description", true);

        assertDoesNotThrow(() -> aCategory.validate(new ThrowsValidationHandler()));

        final var updatedAt = aCategory.getUpdatedAt();
        assertTrue(aCategory.isActive());
        assertNull(aCategory.getDeletedAt());

        final var actualCategory = aCategory.update(expectedName, expectedDescription, expectedIsActive);

        assertDoesNotThrow(() -> aCategory.validate(new ThrowsValidationHandler()));
        assertNotNull(actualCategory.getId());
        assertEquals(expectedIsActive, actualCategory.isActive());
        assertEquals(expectedName, actualCategory.getName());
        assertEquals(expectedDescription, actualCategory.getDescription());
        assertEquals(actualCategory.getCreatedAt(), aCategory.getCreatedAt());
        assertEquals(actualCategory.getId(), aCategory.getId());
        assertTrue(actualCategory.getUpdatedAt().isAfter(updatedAt));
        assertNotNull(actualCategory.getDeletedAt());
    }

    @Test
    void shouldUpdateACategorySuccessfullyEvenWithAnInvalidNameWhenCategoryCallUpdateMethod() {
        final String expectedName = null;
        final var expectedDescription = "Category description";
        final var expectedIsActive = false;

        final var aCategory = Category.newCategory("Movie", "Movie Description", true);

        assertDoesNotThrow(() -> aCategory.validate(new ThrowsValidationHandler()));

        final var updatedAt = aCategory.getUpdatedAt();
        assertTrue(aCategory.isActive());
        assertNull(aCategory.getDeletedAt());

        final var actualCategory = aCategory.update(expectedName, expectedDescription, expectedIsActive);

        assertNotNull(actualCategory.getId());
        assertEquals(expectedIsActive, actualCategory.isActive());
        assertEquals(expectedName, actualCategory.getName());
        assertEquals(expectedDescription, actualCategory.getDescription());
        assertEquals(actualCategory.getCreatedAt(), aCategory.getCreatedAt());
        assertEquals(actualCategory.getId(), aCategory.getId());
        assertTrue(actualCategory.getUpdatedAt().isAfter(updatedAt));
        assertNotNull(actualCategory.getDeletedAt());
    }
}