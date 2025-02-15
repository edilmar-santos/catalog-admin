package com.eddy.admin.catalog.domain.category;

import com.eddy.admin.catalog.domain.validation.Error;
import com.eddy.admin.catalog.domain.validation.ValidationHandler;
import com.eddy.admin.catalog.domain.validation.Validator;

import static java.util.Objects.isNull;

public class CategoryValidator extends Validator {

    public static final int MIN_NAME_LENGTH = 3;
    public static final int MAX_NAME_LENGTH = 255;
    public static final String NAME_SHOULD_NOT_BE_NULL_OR_BLANK = "Name should not be null or blank";
    public static final String NAME_MUST_BE_BETWEEN_3_AND_255_CHARACTERS = "Name must be between 3 and 255 characters";

    private final Category category;

    public CategoryValidator(final Category aCategory, final ValidationHandler handler) {
        super(handler);
        this.category = aCategory;
    }

    @Override
    public void validate() {

        final var categoryName = this.category.getName();
        if (isNull(categoryName) || categoryName.isBlank()) {
            this.validationHandler().append(new Error(NAME_SHOULD_NOT_BE_NULL_OR_BLANK));
            return;
        }

        final var categoryNameLength = categoryName.trim().length();
        if (categoryNameLength < MIN_NAME_LENGTH || categoryNameLength > MAX_NAME_LENGTH) {
            this.validationHandler().append(new Error(NAME_MUST_BE_BETWEEN_3_AND_255_CHARACTERS));
        }
    }
}
