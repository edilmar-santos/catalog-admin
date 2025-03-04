package com.eddy.admin.catalog.e2e.category;


import com.eddy.admin.catalog.E2ETest;
import com.eddy.admin.catalog.domain.category.CategoryID;
import com.eddy.admin.catalog.infrastructure.category.models.CategoryResponse;
import com.eddy.admin.catalog.infrastructure.category.models.CreateCategoryRequest;
import com.eddy.admin.catalog.infrastructure.category.persistence.CategoryRepository;
import com.eddy.admin.catalog.infrastructure.configuration.json.Json;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@E2ETest
@Testcontainers
public class CategoryE2ETest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private CategoryRepository categoryRepository;

    @Container
    private static final MySQLContainer MYSQL_CONTAINER
            = new MySQLContainer("mysql:latest")
            .withUsername("root")
            .withPassword("123456")
            .withDatabaseName("adm_videos");

    @DynamicPropertySource
    public static void setDatasourceProperties(DynamicPropertyRegistry registry) {
        registry.add("mysql.port", () -> MYSQL_CONTAINER.getMappedPort(3306));
    }


    @Test
    public void asACategoryAdminIShouldBeAbleToCreateACategory() throws Exception {
        assertTrue(MYSQL_CONTAINER.isRunning());
        assertEquals(0, categoryRepository.count());

        final var expectedName = "Category Name";
        final var expectedDescription = "Category Description";
        final var expectedIsActive = true;

        final var actualId = givenACategory(expectedName, expectedDescription, expectedIsActive);
        final var categoryResponse = retrieveACategory(actualId.getValue());

        Assertions.assertEquals(expectedName, categoryResponse.name());
        Assertions.assertEquals(expectedDescription, categoryResponse.description());
        Assertions.assertEquals(expectedIsActive, categoryResponse.active());
        Assertions.assertNotNull(categoryResponse.createdAt());
        Assertions.assertNotNull(categoryResponse.updatedAt());
        Assertions.assertNull(categoryResponse.deletedAt());
    }

    private CategoryID givenACategory(final String aName, final String aDescription, final boolean isActive) throws Exception {

        final var categoryRequestBody = new CreateCategoryRequest(aName, aDescription, isActive);

        final var aRequest = MockMvcRequestBuilders.post("/categories")
                .contentType("application/json")
                .content(Json.writeValueAsString(categoryRequestBody));

        final var actualId = mockMvc.perform(aRequest)
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse().getHeader("Location")
                .replace("/categories/", "");

        return CategoryID.from(actualId);
    }

    private CategoryResponse retrieveACategory(final String anId) throws Exception {
        final var aRequest = MockMvcRequestBuilders.get("/categories/" + anId);

        final var categoryResponseJson = mockMvc.perform(aRequest)
                .andExpect(status().isOk())
                .andReturn()
                .getResponse().getContentAsString();

        return Json.readValue(categoryResponseJson, CategoryResponse.class);
    }
}
