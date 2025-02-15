package com.eddy.admin.catalog.domain;

import com.eddy.admin.catalog.domain.validation.ValidationHandler;

import java.util.Objects;

public abstract class Entity<ID extends Identifier>  {

    private final ID id;

    protected Entity(ID id) {
        Objects.requireNonNull(id, "Id cannot be null");
        this.id = id;
    }

    public abstract void validate(ValidationHandler handler);

    public ID getId() {
        return id;
    }

    @Override
    public boolean equals(final Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Entity<?> entity = (Entity<?>) o;
        return Objects.equals(getId(), entity.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }
}
