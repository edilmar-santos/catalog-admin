package com.eddy.admin.catalog.infrastructure.api;

import com.eddy.admin.catalog.domain.pagination.Pagination;
import com.eddy.admin.catalog.infrastructure.category.models.CategoryResponse;
import com.eddy.admin.catalog.infrastructure.category.models.CreateCategoryRequest;
import com.eddy.admin.catalog.infrastructure.category.models.UpdateCategoryRequest;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequestMapping(value = "categories")
@Tag(name = "Categories")
public interface CategoryAPI {
    @PostMapping(
            produces = MediaType.APPLICATION_JSON_VALUE,
            consumes = MediaType.APPLICATION_JSON_VALUE
    )
    @Operation(summary = "Create a new category")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "A Category created successfully"),
            @ApiResponse(responseCode = "422", description = "Invalid data provided in the request"),
            @ApiResponse(responseCode = "500", description = "Unexpected server error")}
    )
    ResponseEntity<?> createCategory(@RequestBody CreateCategoryRequest input);

    @GetMapping
    @Operation(summary = "Retrieve all categories based on filters")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "A Category listed successfully"),
            @ApiResponse(responseCode = "422", description = "Invalid parameter provided in the request"),
            @ApiResponse(responseCode = "500", description = "Unexpected server error")}
    )
    Pagination<?> listCategories(
            @RequestParam(name = "search", required = false, defaultValue = "") final String search,
            @RequestParam(name = "page", required = false, defaultValue = "0") final int page,
            @RequestParam(name = "perPage", required = false, defaultValue = "10") final int perPage,
            @RequestParam(name = "sort", required = false, defaultValue = "name") final String sort,
            @RequestParam(name = "dir", required = false, defaultValue = "asc") final String direction
    );

    @GetMapping("/{id}")
    @Operation(summary = "Retrieve a category by ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "A Category retrieved successfully"),
            @ApiResponse(responseCode = "404", description = "Category not found"),
            @ApiResponse(responseCode = "500", description = "Unexpected server error")}
    )
    CategoryResponse getCategoryById(@PathVariable final String id);

    @PutMapping("/{id}")
    @Operation(summary = "Update category by ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "A Category updated successfully"),
            @ApiResponse(responseCode = "422", description = "Invalid data provided in the request"),
            @ApiResponse(responseCode = "500", description = "Unexpected server error")}
    )
    ResponseEntity<?> updateCategory(@RequestBody UpdateCategoryRequest input, @PathVariable final String id);

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "Delete category by ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "A Category Deleted successfully"),
            @ApiResponse(responseCode = "404", description = "Category not found"),
            @ApiResponse(responseCode = "500", description = "Unexpected server error")}
    )
    void deleteCategory(@PathVariable final String id);
}

