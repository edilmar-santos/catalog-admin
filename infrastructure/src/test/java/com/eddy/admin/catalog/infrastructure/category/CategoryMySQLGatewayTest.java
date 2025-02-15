package com.eddy.admin.catalog.infrastructure.category;

import com.eddy.admin.catalog.domain.category.Category;
import com.eddy.admin.catalog.domain.category.CategoryID;
import com.eddy.admin.catalog.domain.category.CategorySearchQuery;
import com.eddy.admin.catalog.MySQLGatewayTest;
import com.eddy.admin.catalog.infrastructure.category.persistence.CategoryJpaEntity;
import com.eddy.admin.catalog.infrastructure.category.persistence.CategoryRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

@MySQLGatewayTest
public class CategoryMySQLGatewayTest {

    @Autowired
    private CategoryMySQLGateway categoryMySQLGateway;

    @Autowired
    private CategoryRepository categoryRepository;

    @Test
    void shouldCreateACategorySuccessfullyWhenCallCreateMethod() {
        final var expectedName = "Category Name";
        final var expectedDescription = "Category Description";
        final var expectedActive = true;

        final var category = Category.newCategory(expectedName, expectedDescription, expectedActive);

        Assertions.assertEquals(0, categoryRepository.count());

        final var actualCategory = categoryMySQLGateway.create(category);

        Assertions.assertEquals(1, categoryRepository.count());

        Assertions.assertEquals(category.getId().getValue(), actualCategory.getId().getValue());
        Assertions.assertEquals(expectedName, actualCategory.getName());
        Assertions.assertEquals(expectedDescription, actualCategory.getDescription());
        Assertions.assertEquals(expectedActive, actualCategory.isActive());
        Assertions.assertEquals(category.getCreatedAt(), actualCategory.getCreatedAt());
        Assertions.assertEquals(category.getUpdatedAt(), actualCategory.getUpdatedAt());
        Assertions.assertNull(actualCategory.getDeletedAt());

        final var actualEntity = categoryRepository.findById(actualCategory.getId().getValue()).get();

        Assertions.assertEquals(actualCategory.getId().getValue(), actualEntity.getId());
        Assertions.assertEquals(expectedName, actualEntity.getName());
        Assertions.assertEquals(expectedDescription, actualEntity.getDescription());
        Assertions.assertEquals(expectedActive, actualEntity.isActive());
        Assertions.assertEquals(category.getCreatedAt(), actualEntity.getCreatedAt());
        Assertions.assertEquals(category.getUpdatedAt(), actualEntity.getUpdatedAt());
        Assertions.assertNull(actualEntity.getDeletedAt());
    }

    @Test
    void shouldUpdateACategorySuccessfullyWhenCallCreateMethod() {
        final var expectedName = "Category Name";
        final var expectedDescription = "Category Description";
        final var expectedActive = true;

        final var category = Category.newCategory("Old name", "Old Category", true);

        Assertions.assertEquals(0, categoryRepository.count());

        categoryMySQLGateway.create(category);

        Assertions.assertEquals(1, categoryRepository.count());

        final var categoryUpdated = category.clone().update(expectedName, expectedDescription, expectedActive);
        final var actualCategory = categoryMySQLGateway.update(categoryUpdated);

        Assertions.assertEquals(1, categoryRepository.count());

        Assertions.assertEquals(category.getId().getValue(), actualCategory.getId().getValue());
        Assertions.assertEquals(expectedName, actualCategory.getName());
        Assertions.assertEquals(expectedDescription, actualCategory.getDescription());
        Assertions.assertEquals(expectedActive, actualCategory.isActive());
        Assertions.assertEquals(category.getCreatedAt(), actualCategory.getCreatedAt());
        Assertions.assertTrue(category.getUpdatedAt().isBefore(actualCategory.getUpdatedAt()));
        Assertions.assertNull(actualCategory.getDeletedAt());

        final var actualEntity = categoryRepository.findById(actualCategory.getId().getValue()).get();

        Assertions.assertEquals(actualCategory.getId().getValue(), actualEntity.getId());
        Assertions.assertEquals(expectedName, actualEntity.getName());
        Assertions.assertEquals(expectedDescription, actualEntity.getDescription());
        Assertions.assertEquals(expectedActive, actualEntity.isActive());
        Assertions.assertEquals(category.getCreatedAt(), actualEntity.getCreatedAt());
        Assertions.assertTrue(category.getUpdatedAt().isBefore(actualEntity.getUpdatedAt()));
        Assertions.assertNull(actualEntity.getDeletedAt());
    }

    @Test
    void shouldDeleteACategorySuccessfullyWhenCallDeleteMethodWithAndValidCategoryId() {
        final var expectedName = "Category Name";
        final var expectedDescription = "Category Description";
        final var expectedActive = true;

        final var category = Category.newCategory(expectedName, expectedDescription, expectedActive);

        Assertions.assertEquals(0, categoryRepository.count());

        final var actualCategory = categoryMySQLGateway.create(category);

        Assertions.assertEquals(1, categoryRepository.count());

        Assertions.assertEquals(category.getId().getValue(), actualCategory.getId().getValue());
        Assertions.assertEquals(expectedName, actualCategory.getName());
        Assertions.assertEquals(expectedDescription, actualCategory.getDescription());
        Assertions.assertEquals(expectedActive, actualCategory.isActive());
        Assertions.assertEquals(category.getCreatedAt(), actualCategory.getCreatedAt());
        Assertions.assertEquals(category.getUpdatedAt(), actualCategory.getUpdatedAt());
        Assertions.assertNull(actualCategory.getDeletedAt());

        categoryRepository.deleteById(category.getId().getValue());
        Assertions.assertEquals(0, categoryRepository.count());
    }

    @Test
    void shouldNotThrowAnExceptionWhenTryToDeleteWithAnInvalidId() {

        Assertions.assertDoesNotThrow(() ->
                categoryRepository.deleteById(CategoryID.from("InvalidID").getValue()));
    }

    @Test
    void shouldFindACategorySuccessfullyWhenCallGetByIdMethod() {
        final var expectedName = "Category Name";
        final var expectedDescription = "Category Description";
        final var expectedActive = true;

        final var category = Category.newCategory(expectedName, expectedDescription, expectedActive);

        Assertions.assertEquals(0, categoryRepository.count());

        categoryMySQLGateway.create(category);

        Assertions.assertEquals(1, categoryRepository.count());

        final var actualCategory = categoryMySQLGateway.getById(category.getId()).get();

        Assertions.assertEquals(1, categoryRepository.count());

        Assertions.assertEquals(category.getId().getValue(), actualCategory.getId().getValue());
        Assertions.assertEquals(expectedName, actualCategory.getName());
        Assertions.assertEquals(expectedDescription, actualCategory.getDescription());
        Assertions.assertEquals(expectedActive, actualCategory.isActive());
        Assertions.assertEquals(category.getCreatedAt(), actualCategory.getCreatedAt());
        Assertions.assertEquals(category.getUpdatedAt(), actualCategory.getUpdatedAt());
        Assertions.assertNull(actualCategory.getDeletedAt());

        final var actualEntity = categoryRepository.findById(actualCategory.getId().getValue()).get();

        Assertions.assertEquals(actualCategory.getId().getValue(), actualEntity.getId());
        Assertions.assertEquals(expectedName, actualEntity.getName());
        Assertions.assertEquals(expectedDescription, actualEntity.getDescription());
        Assertions.assertEquals(expectedActive, actualEntity.isActive());
        Assertions.assertEquals(category.getCreatedAt(), actualEntity.getCreatedAt());
        Assertions.assertEquals(category.getUpdatedAt(), actualEntity.getUpdatedAt());
        Assertions.assertNull(actualEntity.getDeletedAt());
    }

    @Test
    void shouldReturnEmptyWhenCallGetByIdWithANotPersistedCategoryId() {
        Assertions.assertEquals(0, categoryRepository.count());

        final var actualCategory = categoryMySQLGateway.getById(CategoryID.from("empty"));

        Assertions.assertTrue(actualCategory.isEmpty());
    }


    @Test
    void shouldReturnAllCategorySuccessfullyWhenCallFindAllMethod() {

        final var expectedPage = 0;
        final var expectedPerPage = 1;
        final var expectedTotal = 3;
        final var expectedFirstResult = "Documentary";

        final var film = Category.newCategory("Filme", null, true);
        final var serie = Category.newCategory("Serie", null, true);
        final var documentary = Category.newCategory("Documentary", null, true);

        Assertions.assertEquals(0, categoryRepository.count());

        categoryRepository.saveAll(List.of(
                CategoryJpaEntity.from(film),
                CategoryJpaEntity.from(serie),
                CategoryJpaEntity.from(documentary)
        ));

        Assertions.assertEquals(3, categoryRepository.count());

        final var searchQuery = new CategorySearchQuery(expectedPage, expectedPerPage, " ", "name", "asc");
        final var actualCategories = categoryMySQLGateway.findAll(searchQuery);

        Assertions.assertEquals(expectedPage, actualCategories.currentPage());
        Assertions.assertEquals(expectedPerPage, actualCategories.perPage());
        Assertions.assertEquals(expectedTotal, actualCategories.total());
        Assertions.assertEquals(expectedFirstResult, actualCategories.items().getFirst().getName());
    }

    @Test
    void shouldReturnEmptyWhenCallFindAllWithAnyPersistedCategory() {
        final var expectedPage = 0;
        final var expectedPerPage = 1;
        final var expectedTotal = 0;

        final var searchQuery = new CategorySearchQuery(expectedPage, expectedPerPage, " ", "name", "asc");
        final var actualCategories = categoryMySQLGateway.findAll(searchQuery);

        Assertions.assertEquals(expectedPage, actualCategories.currentPage());
        Assertions.assertEquals(expectedPerPage, actualCategories.perPage());
        Assertions.assertEquals(expectedTotal, actualCategories.total());
    }

    @Test
    public void shouldReturnCategoryPaginatedWhenCallFindAllWithPage1() {
        var expectedPage = 0;
        final var expectedPerPage = 1;
        final var expectedTotal = 3;

        final var movies = Category.newCategory("Movies", null, true);
        final var series = Category.newCategory("Séries", null, true);
        final var documentary = Category.newCategory("Documentary", null, true);

        Assertions.assertEquals(0, categoryRepository.count());

        categoryRepository.saveAll(List.of(
                CategoryJpaEntity.from(movies),
                CategoryJpaEntity.from(series),
                CategoryJpaEntity.from(documentary)
        ));

        Assertions.assertEquals(3, categoryRepository.count());

        var query = new CategorySearchQuery(0, 1, "", "name", "asc");
        var actualResult = categoryMySQLGateway.findAll(query);

        Assertions.assertEquals(expectedPage, actualResult.currentPage());
        Assertions.assertEquals(expectedPerPage, actualResult.perPage());
        Assertions.assertEquals(expectedTotal, actualResult.total());
        Assertions.assertEquals(expectedPerPage, actualResult.items().size());
        Assertions.assertEquals(documentary.getId(), actualResult.items().get(0).getId());

        // Page 1
        expectedPage = 1;

        query = new CategorySearchQuery(1, 1, "", "name", "asc");
        actualResult = categoryMySQLGateway.findAll(query);

        Assertions.assertEquals(expectedPage, actualResult.currentPage());
        Assertions.assertEquals(expectedPerPage, actualResult.perPage());
        Assertions.assertEquals(expectedTotal, actualResult.total());
        Assertions.assertEquals(expectedPerPage, actualResult.items().size());
        Assertions.assertEquals(movies.getId(), actualResult.items().get(0).getId());

        // Page 2
        expectedPage = 2;

        query = new CategorySearchQuery(2, 1, "", "name", "asc");
        actualResult = categoryMySQLGateway.findAll(query);

        Assertions.assertEquals(expectedPage, actualResult.currentPage());
        Assertions.assertEquals(expectedPerPage, actualResult.perPage());
        Assertions.assertEquals(expectedTotal, actualResult.total());
        Assertions.assertEquals(expectedPerPage, actualResult.items().size());
        Assertions.assertEquals(series.getId(), actualResult.items().get(0).getId());
    }

    @Test
    public void shouldReturnCategoryMatchingTermsWhenCallFindAllWithNameAsTermPaginated() {
        final var expectedPage = 0;
        final var expectedPerPage = 1;
        final var expectedTotal = 1;

        final var movie = Category.newCategory("Filmes", null, true);
        final var series = Category.newCategory("Séries", null, true);
        final var documentary = Category.newCategory("Documentary", null, true);

        Assertions.assertEquals(0, categoryRepository.count());

        categoryRepository.saveAll(List.of(
                CategoryJpaEntity.from(movie),
                CategoryJpaEntity.from(series),
                CategoryJpaEntity.from(documentary)
        ));

        Assertions.assertEquals(3, categoryRepository.count());

        final var query = new CategorySearchQuery(0, 1, "doc", "name", "asc");
        final var actualResult = categoryMySQLGateway.findAll(query);

        Assertions.assertEquals(expectedPage, actualResult.currentPage());
        Assertions.assertEquals(expectedPerPage, actualResult.perPage());
        Assertions.assertEquals(expectedTotal, actualResult.total());
        Assertions.assertEquals(expectedPerPage, actualResult.items().size());
        Assertions.assertEquals(documentary.getId(), actualResult.items().get(0).getId());
    }

    @Test
    public void shouldReturnCategoryMatchingTermsWhenCallFindAllWithDescriptionAsTermPaginated() {
        final var expectedPage = 0;
        final var expectedPerPage = 1;
        final var expectedTotal = 1;

        final var movie = Category.newCategory("Movies", "The category most watched", true);
        final var series = Category.newCategory("Series", "The category watched", true);
        final var documentary = Category.newCategory("Documentary", "The category least watched", true);

        Assertions.assertEquals(0, categoryRepository.count());

        categoryRepository.saveAll(List.of(
                CategoryJpaEntity.from(movie),
                CategoryJpaEntity.from(series),
                CategoryJpaEntity.from(documentary)
        ));

        Assertions.assertEquals(3, categoryRepository.count());

        final var query = new CategorySearchQuery(0, 1, "MOST WATCHED", "name", "asc");
        final var actualResult = categoryMySQLGateway.findAll(query);

        Assertions.assertEquals(expectedPage, actualResult.currentPage());
        Assertions.assertEquals(expectedPerPage, actualResult.perPage());
        Assertions.assertEquals(expectedTotal, actualResult.total());
        Assertions.assertEquals(expectedPerPage, actualResult.items().size());
        Assertions.assertEquals(movie.getId(), actualResult.items().get(0).getId());
    }
}



