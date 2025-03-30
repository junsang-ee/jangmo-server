package com.jangmo.web.config.dsl;


import org.jetbrains.annotations.NotNull;
import org.springframework.data.jpa.repository.support.JpaRepositoryFactoryBean;
import org.springframework.data.repository.Repository;
import org.springframework.data.repository.core.support.RepositoryFactorySupport;

import javax.persistence.EntityManager;
import java.io.Serializable;

public class QueryDslJpaRepositoryFactoryBean<T extends Repository<S, I>, S, I extends Serializable>
    extends JpaRepositoryFactoryBean<T, S, I> {

    public QueryDslJpaRepositoryFactoryBean(Class<? extends T> repositoryInterface) {
        super(repositoryInterface);
    }

    @NotNull
    @Override
    protected RepositoryFactorySupport createRepositoryFactory(@NotNull EntityManager entityManager) {
        return new QueryDslRepositoryFactory(entityManager);
    }

}
