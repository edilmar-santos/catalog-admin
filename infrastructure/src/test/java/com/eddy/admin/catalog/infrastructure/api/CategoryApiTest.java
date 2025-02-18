package com.eddy.admin.catalog.infrastructure.api;

import com.eddy.admin.catalog.ControllerTest;
import com.eddy.admin.catalog.application.category.create.CreateCategoryOutput;
import com.eddy.admin.catalog.application.category.create.CreateCategoryUseCase;
import com.eddy.admin.catalog.domain.exceptions.DomainException;
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
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.argThat;
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
}
