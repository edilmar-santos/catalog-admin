package com.eddy.admin.catalog.infrastructure.category.models;

import com.fasterxml.jackson.annotation.JsonProperty;

public record CreateCategoryApiInput(
        @JsonProperty("name") String name,
        @JsonProperty("description") String description,
        @JsonProperty(value = "is_active", defaultValue = "true") boolean active
) {
}
