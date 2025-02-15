package com.eddy.admin.catalog.domain.validation.handler;

import com.eddy.admin.catalog.domain.exceptions.DomainException;
import com.eddy.admin.catalog.domain.validation.Error;
import com.eddy.admin.catalog.domain.validation.ValidationHandler;

import java.util.ArrayList;
import java.util.List;

public class Notification implements ValidationHandler {

    private final List<Error> errors;

    private Notification (List<Error> errors) {
        this.errors = errors;
    }

    public static Notification create() {
        return new Notification(new ArrayList<>());
    }

    public static Notification create(Error error) {
        return new Notification(new ArrayList<>()).append(error);
    }

    public static Notification create(Throwable throwable) {
        return create(new Error(throwable.getMessage()));
    }

    @Override
    public Notification append(Error error) {
        this.errors.add(error);
        return this;
    }

    @Override
    public Notification append(ValidationHandler handler) {
        this.errors.addAll(handler.getErrors());
        return this;
    }

    @Override
    public Notification validate(Validation validation) {

        try {
            validation.validate();
        } catch (DomainException e) {
            this.errors.addAll(e.getErrors());
        } catch (Throwable t) {
            this.errors.add(new Error(t.getMessage()));
        }
        return this;
    }

    @Override
    public List<Error> getErrors() {
        return this.errors;
    }
}
