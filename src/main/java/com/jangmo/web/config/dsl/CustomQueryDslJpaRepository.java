package com.jangmo.web.config.dsl;

import com.querydsl.core.types.EntityPath;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.Predicate;
import com.querydsl.core.types.dsl.PathBuilder;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.support.JpaEntityInformation;
import org.springframework.data.jpa.repository.support.Querydsl;
import org.springframework.data.jpa.repository.support.QuerydslJpaRepository;
import org.springframework.data.querydsl.EntityPathResolver;
import org.springframework.data.querydsl.QSort;
import org.springframework.data.querydsl.SimpleEntityPathResolver;

import javax.persistence.EntityManager;
import java.io.Serializable;
import java.util.List;

class CustomQueryDslJpaRepository<T, I extends Serializable>
    extends QuerydslJpaRepository<T, I>
    implements QueryDslPredicateExtendedExecutor<T> {

    private static final EntityPathResolver DEFAULT_ENTITY_PATH_RESOLVER = SimpleEntityPathResolver.INSTANCE;

    private EntityPath<T> path;

    private Querydsl querydsl;

    CustomQueryDslJpaRepository(JpaEntityInformation<T, I> entityInformation,
                                EntityManager entityManager) {
        super(entityInformation, entityManager);
        this.path = DEFAULT_ENTITY_PATH_RESOLVER.createPath(entityInformation.getJavaType());
        PathBuilder<T> builder = new PathBuilder<T>(path.getType(), path.getMetadata());
        this.querydsl = new Querydsl(entityManager, builder);
    }

    @Override
    public List<T> findLimit(Predicate predicate, long limit) {
        return createQuery(predicate).select(path).limit(limit).fetch();
    }

    @Override
    public List<T> findLimit(Predicate predicate, long limit, Sort sort) {
        return querydsl.applySorting(
                sort, createQuery(predicate).select(path)
        ).limit(limit).fetch();
    }

    @Override
    public List<T> findLimit(Predicate predicate, long limit, OrderSpecifier<?>... orders) {
        QSort sort = new QSort(orders);
        return findLimit(predicate, limit, sort);
    }
}
