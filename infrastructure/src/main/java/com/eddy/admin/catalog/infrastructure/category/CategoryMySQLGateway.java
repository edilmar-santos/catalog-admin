package com.eddy.admin.catalog.infrastructure.category;

import com.eddy.admin.catalog.domain.category.Category;
import com.eddy.admin.catalog.domain.category.CategoryGateway;
import com.eddy.admin.catalog.domain.category.CategoryID;
import com.eddy.admin.catalog.domain.category.CategorySearchQuery;
import com.eddy.admin.catalog.domain.pagination.Pagination;
import com.eddy.admin.catalog.infrastructure.category.persistence.CategoryJpaEntity;
import com.eddy.admin.catalog.infrastructure.category.persistence.CategoryRepository;
import com.eddy.admin.catalog.infrastructure.utils.SpecificationUtils;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Repository;

import java.util.Optional;

import static java.util.Optional.ofNullable;

@Repository
public class CategoryMySQLGateway implements CategoryGateway {

    final CategoryRepository repository;

    public CategoryMySQLGateway(final CategoryRepository categoryRepository) {
        this.repository = categoryRepository;
    }

    @Override
    public Category create(final Category category) {
        return save(category);
    }

    @Override
    public void deleteById(CategoryID id) {
        final var anIdValue = id.getValue();
        if (this.repository.existsById(anIdValue)) {
            this.repository.deleteById(id.getValue());
        }
    }

    @Override
    public Optional<Category> getById(CategoryID id) {

        return repository.findById(id.getValue())
                .map(CategoryJpaEntity::toAggregate);
    }

    @Override
    public Category update(Category category) {
        return save(category);
    }

    @Override
    public Pagination<Category> findAll(final CategorySearchQuery query) {

        final var page = PageRequest.of(
                query.page(),
                query.perPage(),
                Sort.by(Sort.Direction.fromString(query.direction()), query.sort())
        );

        final var specification = ofNullable(query.terms())
                .filter(str -> !str.isBlank())
                .map(str -> {
                    final Specification<CategoryJpaEntity> nameLike = SpecificationUtils.like("name", str);
                    final Specification<CategoryJpaEntity> descriptionLike = SpecificationUtils.like("description", str);

                    return nameLike.or(descriptionLike);
                }).orElse(null);


        final var pageResult = repository.findAll(specification, page);

        return new Pagination<>(
                pageResult.getNumber(),
                pageResult.getSize(),
                pageResult.getTotalElements(),
                pageResult.map(CategoryJpaEntity::toAggregate).toList()
        );
    }

    private Category save(Category category) {
        return repository.save(CategoryJpaEntity.from(category)).toAggregate();
    }
}
