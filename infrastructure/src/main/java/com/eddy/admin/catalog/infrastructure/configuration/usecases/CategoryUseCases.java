package com.eddy.admin.catalog.infrastructure.configuration.usecases;

import com.eddy.admin.catalog.application.category.create.DefaultCreateCategoryUseCase;
import com.eddy.admin.catalog.application.category.delete.DefaultDeleteCategoryUseCase;
import com.eddy.admin.catalog.application.category.retrieve.get.DefaultGetCategoryByIdUseCase;
import com.eddy.admin.catalog.application.category.retrieve.list.DefaultListCategoryUseCase;
import com.eddy.admin.catalog.application.category.update.DefaultUpdateCategoryUseCase;
import com.eddy.admin.catalog.domain.category.CategoryGateway;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class CategoryUseCases {

    private final CategoryGateway gateway;

    public CategoryUseCases(CategoryGateway gateway) {
        this.gateway = gateway;
    }

    @Bean
    public DefaultCreateCategoryUseCase getDefaultCreateCategoryUseCase() {
        return new DefaultCreateCategoryUseCase(this.gateway);
    }

    @Bean
    public DefaultDeleteCategoryUseCase getDefaultDeleteCategoryUseCase() {
        return new DefaultDeleteCategoryUseCase(this.gateway);
    }

    @Bean
    public DefaultGetCategoryByIdUseCase getDefaultGetCategoryByIdUseCase() {
        return new DefaultGetCategoryByIdUseCase(this.gateway);
    }

    @Bean
    public DefaultListCategoryUseCase getDefaultListCategoryUseCase() {
        return new DefaultListCategoryUseCase(this.gateway);
    }

    @Bean
    public DefaultUpdateCategoryUseCase getDefaultUpdateCategoryUseCase() {
        return new DefaultUpdateCategoryUseCase(this.gateway);
    }
}
