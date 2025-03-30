package com.jangmo.web.config.dsl;

import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.Predicate;
import org.jetbrains.annotations.NotNull;
import org.springframework.data.domain.Sort;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;

import javax.annotation.Nonnegative;
import java.util.List;

public interface QueryDslPredicateExtendedExecutor<T> extends QuerydslPredicateExecutor<T> {

    @NotNull
    @Override
    List<T> findAll(@NotNull Predicate predicate);

    @NotNull
    @Override
    List<T> findAll(@NotNull Predicate predicate, @NotNull Sort sort);

    @NotNull
    @Override
    List<T> findAll(@NotNull Predicate predicate, @NotNull OrderSpecifier<?>... orders);

    @NotNull
    @Override
    List<T> findAll(@NotNull OrderSpecifier<?>... orders);

    /**
     * Returns limited entries matching the given {@link Predicate}. In case no match could be found
     * an empty {@link List} is returned.
     *
     * @param predicate predicate
     * @param limit maximum fetch limit
     * @return entries matching the given {@link Predicate}.
     */
    List<T> findLimit(Predicate predicate, @Nonnegative long limit);

    /**
     * Returns limited entries matching the given {@link Predicate} applying the given {@link Sort}.
     * In case no match could be found an empty {@link List} is returned.
     *
     * @param predicate predicate
     * @param limit maximum fetch limit
     * @param sort the {@link Sort} specification to sort the results by, must not be {@literal null}.
     * @return entries matching the given {@link Predicate}.
     */
    List<T> findLimit(Predicate predicate, @Nonnegative long limit, Sort sort);

    /**
     * Returns limited entries matching the given {@link Predicate} applying the given
     * {@link OrderSpecifier}. In case no match could be found an empty {@link List} is returned.
     *
     * @param predicate predicate
     * @param limit maximum fetch limit
     * @param orders the {@link OrderSpecifier} to sort the results by, must not be {@literal null}.
     * @return entries matching the given {@link Predicate}.
     */
    List<T> findLimit(Predicate predicate, @Nonnegative long limit, OrderSpecifier<?>... orders);
}
