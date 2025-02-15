package com.eddy.admin.catalog.infrastructure.category.persistence;

import com.eddy.admin.catalog.domain.category.Category;
import com.eddy.admin.catalog.MySQLGatewayTest;
import org.hibernate.PropertyValueException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;

@MySQLGatewayTest
public class CategoryRepositoryTest {

    @Autowired
    private CategoryRepository categoryRepository;

    @Test
    void shouldReturnErrorWhenPropertyNameIsNull() {
        final var aCategory = Category.newCategory("Movie", "The best movie", true);
        final var categoryEntity = CategoryJpaEntity.from(aCategory);
        final var expectedProperty= "name";
        final var expectedErrorMessage = "not-null property references a null or transient value : com.eddy.admin.catalog.infrastructure.category.persistence.CategoryJpaEntity.name";
        categoryEntity.setName(null);

        final var actualException =
                Assertions.assertThrows(DataIntegrityViolationException.class, () -> categoryRepository.save(categoryEntity));

        final var actualCause = Assertions.assertInstanceOf(PropertyValueException.class, actualException.getCause());

        Assertions.assertEquals(expectedProperty, actualCause.getPropertyName());
        Assertions.assertEquals(expectedErrorMessage, actualCause.getMessage());
    }

    @Test
    void shouldReturnErrorWhenPropertyCreatedAtIsNull() {
        final var aCategory = Category.newCategory("Movie", "The best movie", true);
        final var categoryEntity = CategoryJpaEntity.from(aCategory);
        final var expectedProperty= "createdAt";
        final var expectedErrorMessage = "not-null property references a null or transient value : com.eddy.admin.catalog.infrastructure.category.persistence.CategoryJpaEntity.createdAt";
        categoryEntity.setCreatedAt(null);

        final var actualException =
                Assertions.assertThrows(DataIntegrityViolationException.class, () -> categoryRepository.save(categoryEntity));

        final var actualCause = Assertions.assertInstanceOf(PropertyValueException.class, actualException.getCause());

        Assertions.assertEquals(expectedProperty, actualCause.getPropertyName());
        Assertions.assertEquals(expectedErrorMessage, actualCause.getMessage());
    }

    @Test
    void shouldReturnErrorWhenPropertyUpdateAtIsNull() {
        final var aCategory = Category.newCategory("Movie", "The best movie", true);
        final var categoryEntity = CategoryJpaEntity.from(aCategory);
        final var expectedProperty= "updatedAt";
        final var expectedErrorMessage = "not-null property references a null or transient value : com.eddy.admin.catalog.infrastructure.category.persistence.CategoryJpaEntity.updatedAt";
        categoryEntity.setUpdatedAt(null);

        final var actualException =
                Assertions.assertThrows(DataIntegrityViolationException.class, () -> categoryRepository.save(categoryEntity));

        final var actualCause = Assertions.assertInstanceOf(PropertyValueException.class, actualException.getCause());

        Assertions.assertEquals(expectedProperty, actualCause.getPropertyName());
        Assertions.assertEquals(expectedErrorMessage, actualCause.getMessage());
    }
}
