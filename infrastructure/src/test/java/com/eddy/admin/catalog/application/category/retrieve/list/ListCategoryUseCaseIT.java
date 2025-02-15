package com.eddy.admin.catalog.application.category.retrieve.list;

import com.eddy.admin.catalog.IntegrationTest;
import com.eddy.admin.catalog.domain.category.Category;
import com.eddy.admin.catalog.domain.category.CategorySearchQuery;
import com.eddy.admin.catalog.infrastructure.category.persistence.CategoryJpaEntity;
import com.eddy.admin.catalog.infrastructure.category.persistence.CategoryRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.stream.Stream;

@IntegrationTest
public class ListCategoryUseCaseIT {

    @Autowired
    private CategoryRepository repository;

    @Autowired
    private ListCategoryUseCase useCase;

    @BeforeEach
    void mockUp() {
        final var categories = Stream.of(
                        Category.newCategory("Films", null, true),
                        Category.newCategory("Netflix Originals", "Netflix Titles", true),
                        Category.newCategory("Amazon Originals", "Amazon Prime Titles", true),
                        Category.newCategory("Documentary", null, true),
                        Category.newCategory("Sports", null, true),
                        Category.newCategory("Kids", "Category for kids", true),
                        Category.newCategory("Series", null, true)
                )
                .map(CategoryJpaEntity::from)
                .toList();

        repository.saveAllAndFlush(categories);
    }

    @ParameterizedTest
    @CsvSource({
            "fil,0,10,1,1,Films",
            "net,0,10,1,1,Netflix Originals",
            "ZON,0,10,1,1,Amazon Originals",
            "KI,0,10,1,1,Kids",
            "Kids,0,10,1,1,Kids",
            "Amazon pr,0,10,1,1,Amazon Originals",
    })
    void shouldReturnFilteredCategoriesWhenSetValidFilters(
            final String expectedTerms,
            final int expectedPage,
            final int expectedPerPage,
            final int expectedItemsCount,
            final long expectedTotal,
            final String expectedCategoryName
    ) {
        final var expectedSort = "name";
        final var expectedDirection = "asc";

        final var aQuery =
                new CategorySearchQuery(expectedPage, expectedPerPage, expectedTerms, expectedSort, expectedDirection);

        final var actualResult = useCase.execute(aQuery);

        Assertions.assertEquals(expectedItemsCount, actualResult.items().size());
        Assertions.assertEquals(expectedPage, actualResult.currentPage());
        Assertions.assertEquals(expectedPerPage, actualResult.perPage());
        Assertions.assertEquals(expectedTotal, actualResult.total());
        Assertions.assertEquals(expectedCategoryName, actualResult.items().get(0).name());
    }
}
