package com.eddy.admin.catalog.infrastructure.api;

import com.eddy.admin.catalog.ControllerTest;
import com.eddy.admin.catalog.application.category.create.CreateCategoryOutput;
import com.eddy.admin.catalog.application.category.create.CreateCategoryUseCase;
import com.eddy.admin.catalog.application.category.retrieve.get.CategoryOutput;
import com.eddy.admin.catalog.application.category.retrieve.get.GetCategoryByIdUseCase;
import com.eddy.admin.catalog.domain.category.Category;
import com.eddy.admin.catalog.domain.category.CategoryID;
import com.eddy.admin.catalog.domain.exceptions.DomainException;
import com.eddy.admin.catalog.domain.exceptions.NotFoundException;
import com.eddy.admin.catalog.domain.validation.Error;
import com.eddy.admin.catalog.domain.validation.handler.Notification;
import com.eddy.admin.catalog.infrastructure.category.models.CreateCategoryApiInput;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.vavr.API;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.Objects;

import static com.eddy.admin.catalog.domain.category.CategoryValidator.NAME_SHOULD_NOT_BE_NULL_OR_BLANK;
import static org.hamcrest.Matchers.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.times;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ControllerTest(controllers = CategoryAPI.class)
public class CategoryApiTest {

    @Autowired
    private MockMvc mvc;

    @Autowired
    private ObjectMapper mapper;

    @MockBean
    private CreateCategoryUseCase createCategoryUseCase;

    @MockBean
    private GetCategoryByIdUseCase getCategoryByIdUseCase;

    @Test
    void shouldCreateACategorySuccessfullyWhenRequestByPostMethod() throws Exception {
        final var expectedName = "Movie";
        final var expectedDescription = "Best Movie";
        final var expectedIsActive = true;

        final var aInput = new CreateCategoryApiInput(expectedName, expectedDescription, expectedIsActive);

        Mockito.when(createCategoryUseCase.execute(any()))
                .thenReturn(API.Right(CreateCategoryOutput.from("123")));

        final var request = MockMvcRequestBuilders.post("/categories")
                .contentType(MediaType.APPLICATION_JSON)
                .content(this.mapper.writeValueAsString(aInput));

        this.mvc.perform(request)
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(header().string("Content-Type", MediaType.APPLICATION_JSON_VALUE))
                .andExpect(header().string("Location", "/categories/123"))
                .andExpect(jsonPath("$.id", equalTo("123")));

        Mockito.verify(createCategoryUseCase, times(1)).execute(argThat(cmd ->
                Objects.equals(expectedName, cmd.name())
                        && Objects.equals(expectedDescription, cmd.description())
                        && Objects.equals(expectedIsActive, cmd.isActive())
        ));
    }

    @Test
    void shouldReturnNotificationWhenGetAnInvalidInput() throws Exception {
        final String expectedName = null;
        final var expectedDescription = "Best Movie";
        final var expectedIsActive = true;

        final var aInput = new CreateCategoryApiInput(expectedName, expectedDescription, expectedIsActive);

        Mockito.when(createCategoryUseCase.execute(any()))
                .thenReturn(API.Left(Notification.create(new Error(NAME_SHOULD_NOT_BE_NULL_OR_BLANK))));

        final var request = MockMvcRequestBuilders.post("/categories")
                .contentType(MediaType.APPLICATION_JSON)
                .content(this.mapper.writeValueAsString(aInput));

        this.mvc.perform(request)
                .andDo(print())
                .andExpect(status().isUnprocessableEntity())
                .andExpect(header().string("Content-Type", MediaType.APPLICATION_JSON_VALUE))
                .andExpect(header().string("Location", nullValue()))
                .andExpect(jsonPath("$.errors", hasSize(1)))
                .andExpect(jsonPath("$.errors[0].message", equalTo(NAME_SHOULD_NOT_BE_NULL_OR_BLANK)));

        Mockito.verify(createCategoryUseCase, times(1)).execute(argThat(cmd ->
                Objects.equals(expectedName, cmd.name())
                        && Objects.equals(expectedDescription, cmd.description())
                        && Objects.equals(expectedIsActive, cmd.isActive())
        ));
    }

    @Test
    void shouldThrowDomainExceptionWhenGetError() throws Exception {
        final String expectedName = null;
        final var expectedDescription = "Best Movie";
        final var expectedIsActive = true;

        final var aInput = new CreateCategoryApiInput(expectedName, expectedDescription, expectedIsActive);

        Mockito.when(createCategoryUseCase.execute(any()))
                .thenThrow(DomainException.with(new Error(NAME_SHOULD_NOT_BE_NULL_OR_BLANK)));

        final var request = MockMvcRequestBuilders.post("/categories")
                .contentType(MediaType.APPLICATION_JSON)
                .content(this.mapper.writeValueAsString(aInput));

        this.mvc.perform(request)
                .andDo(print())
                .andExpect(status().isUnprocessableEntity())
                .andExpect(header().string("Content-Type", MediaType.APPLICATION_JSON_VALUE))
                .andExpect(header().string("Location", nullValue()))
                .andExpect(jsonPath("$.errors", hasSize(1)))
                .andExpect(jsonPath("$.errors[0].message", equalTo(NAME_SHOULD_NOT_BE_NULL_OR_BLANK)));

        Mockito.verify(createCategoryUseCase, times(1)).execute(argThat(cmd ->
                Objects.equals(expectedName, cmd.name())
                        && Objects.equals(expectedDescription, cmd.description())
                        && Objects.equals(expectedIsActive, cmd.isActive())
        ));
    }

    @Test
    void shouldReturnACategorySuccessfullyWhenGetByAValidId() throws Exception {
        final var expectedName = "Movie";
        final var expectedDescription = "Best Movie";
        final var expectedIsActive = true;

        final var aCategory = Category.newCategory(expectedName, expectedDescription, expectedIsActive);
        final var expectedId = aCategory.getId().getValue();

        Mockito.when(getCategoryByIdUseCase.execute(any())).thenReturn(CategoryOutput.from(aCategory));

        final var request = MockMvcRequestBuilders.get("/categories/{id}", expectedId);

        final var response = this.mvc.perform(request)
                .andDo(print());

        response.andExpect(status().isOk())
                .andExpect(jsonPath("$.id", equalTo(expectedId)))
                .andExpect(jsonPath("$.name", equalTo(expectedName)))
                .andExpect(jsonPath("$.description", equalTo(expectedDescription)))
                .andExpect(jsonPath("$.is_active", equalTo(expectedIsActive)))
                .andExpect(jsonPath("$.created_at", equalTo(aCategory.getCreatedAt().toString())))
                .andExpect(jsonPath("$.updated_at", equalTo(aCategory.getUpdatedAt().toString())))
                .andExpect(jsonPath("$.deleted_at", nullValue()));

        Mockito.verify(getCategoryByIdUseCase, times(1)).execute(argThat(categoryId ->
                Objects.equals(expectedId, categoryId)));
    }

    @Test
    void shouldThrowNotFoundExceptionWhenNoneCategoryIsFound() throws Exception {
        final var invalidId = "123";
        final var expectedErrorMessage = "Category ID %s not found.".formatted(invalidId);

        Mockito.when(getCategoryByIdUseCase.execute(any()))
                .thenThrow(NotFoundException.with(Category.class, CategoryID.from(invalidId)));

        final var request = MockMvcRequestBuilders.get("/categories/{id}", invalidId);

        final var response = this.mvc.perform(request)
                .andDo(print());

        response.andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message", equalTo(expectedErrorMessage)));
    }
}
