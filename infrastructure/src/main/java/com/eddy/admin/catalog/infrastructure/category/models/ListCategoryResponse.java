package com.eddy.admin.catalog.infrastructure.category.models;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.Instant;

public record ListCategoryResponse(
        @JsonProperty("id") String id,
        @JsonProperty("name") String name,
        @JsonProperty("description") String description,
        @JsonProperty(value = "is_active", defaultValue = "true") boolean active,
        @JsonProperty("created_at") Instant createdAt,
        @JsonProperty("deleted_at") Instant deletedAt
) {
}
