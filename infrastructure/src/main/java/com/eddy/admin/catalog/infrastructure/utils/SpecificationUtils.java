package com.eddy.admin.catalog.infrastructure.utils;

import com.eddy.admin.catalog.infrastructure.category.persistence.CategoryJpaEntity;
import org.springframework.data.jpa.domain.Specification;

public final class SpecificationUtils {

    private SpecificationUtils() {
    }

    public static Specification<CategoryJpaEntity> like(final String prop, final String term) {
        return (root, query1, cb) -> cb.like(cb.upper(root.get(prop)), like(term.toUpperCase()));
    }

    private static String like(String term) {
        return "%" + term + "%";
    }
}
