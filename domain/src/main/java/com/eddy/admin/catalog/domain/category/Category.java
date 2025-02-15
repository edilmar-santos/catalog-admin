package com.eddy.admin.catalog.domain.category;

import com.eddy.admin.catalog.domain.Entity;
import com.eddy.admin.catalog.domain.validation.ValidationHandler;

import java.time.Instant;
import java.util.Objects;

import static java.util.Objects.requireNonNull;

public class Category extends Entity<CategoryID> implements Cloneable {

    private String name;
    private String description;
    private boolean active;
    private Instant createdAt;
    private Instant updatedAt;
    private Instant deletedAt;

    private Category(
            CategoryID anId,
            String aName,
            String aDescription,
            boolean isActive,
            Instant aCreationDate,
            Instant anUpdateDate,
            Instant aDeleteDate) {
        super(anId);
        this.name = aName;
        this.description = aDescription;
        this.active = isActive;
        this.createdAt = requireNonNull(aCreationDate, "'createdAt' cannot be null");
        this.updatedAt = requireNonNull(anUpdateDate, "'updatedAt' cannot be null");
        this.deletedAt = aDeleteDate;
    }

    public static Category newCategory(final String aName, final String aDescription, final boolean isActive) {
        final var id = CategoryID.unique();
        final var now = Instant.now();
        final var aDeletedDate = isActive ? null : now;
        return new Category(id, aName, aDescription, isActive, now, now, aDeletedDate);
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public boolean isActive() {
        return active;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public Instant getUpdatedAt() {
        return updatedAt;
    }

    public Instant getDeletedAt() {
        return deletedAt;
    }


    public static Category with(
            final CategoryID anId,
            final String name,
            final String description,
            final boolean active,
            final Instant createdAt,
            final Instant updatedAt,
            final Instant deletedAt
    ) {
        return new Category(
                anId,
                name,
                description,
                active,
                createdAt,
                updatedAt,
                deletedAt
        );
    }

    public static Category with(final Category aCategory) {
        return with(
                aCategory.getId(),
                aCategory.name,
                aCategory.description,
                aCategory.isActive(),
                aCategory.createdAt,
                aCategory.updatedAt,
                aCategory.deletedAt
        );
    }

    @Override
    public void validate(ValidationHandler handler) {
        new CategoryValidator(this, handler).validate();
    }

    public Category deactivate() {
        final var now = Instant.now();
        if (Objects.isNull(this.deletedAt)) {
            this.deletedAt = now;
        }

        this.active = false;
        this.updatedAt = now;
        return this;
    }

    public Category activate() {

        this.active = true;
        this.updatedAt = Instant.now();
        this.deletedAt = null;
        return this;
    }

    public Category update(String name, String description, boolean isActive) {

        if (isActive) {
            activate();
        } else {
            deactivate();
        }

        this.name = name;
        this.description = description;
        this.updatedAt = Instant.now();
        return this;
    }

    @Override
    public Category clone() {
        try {
            return (Category) super.clone();
        } catch (CloneNotSupportedException e) {
            throw new AssertionError();
        }
    }
}
