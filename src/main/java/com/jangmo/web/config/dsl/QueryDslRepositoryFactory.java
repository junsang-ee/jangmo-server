package com.jangmo.web.config.dsl;

import org.jetbrains.annotations.NotNull;
import org.springframework.data.jpa.repository.support.JpaRepositoryFactory;
import org.springframework.data.repository.core.RepositoryMetadata;

import javax.persistence.EntityManager;

class QueryDslRepositoryFactory extends JpaRepositoryFactory {

    QueryDslRepositoryFactory(EntityManager entityManager) {
        super(entityManager);
    }

    @NotNull
    @Override
    protected Class<?> getRepositoryBaseClass(RepositoryMetadata metadata) {
        if (isQueryDslExtendedExecutor(metadata.getRepositoryInterface()))
            return CustomQueryDslJpaRepository.class;
        else
            return super.getRepositoryBaseClass(metadata);
    }

    private boolean isQueryDslExtendedExecutor(Class<?> repositoryInterface) {
        return QueryDslPredicateExtendedExecutor.class.isAssignableFrom(repositoryInterface);
    }
}
