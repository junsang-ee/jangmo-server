package com.jangmo.web.config.dsl;

import com.google.common.collect.ImmutableSet;
import com.querydsl.core.types.ExpressionUtils;
import com.querydsl.core.types.Predicate;
import com.querydsl.core.types.dsl.*;

import java.time.LocalDate;
import java.time.temporal.Temporal;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

import lombok.NonNull;

import static com.google.common.collect.Lists.newArrayList;
import static java.util.Optional.ofNullable;
import static org.springframework.util.CollectionUtils.isEmpty;


public abstract class Criteria {
    private static <T> Predicate optional(T value, Function<T, Predicate> func) {
        return ofNullable(value).map(func).orElse(null);
    }

    /**
     * Generates 'equal' predicate.
     *
     * @param path  must not be {@literal null}
     * @param value matching value
     * @return {@literal null} if {@code value} is null
     */
    protected static <T> Predicate eq(@NonNull SimpleExpression<T> path, T value) {
        return optional(value, path::eq);
    }

    /**
     * Generates 'not equal' predicate.
     *
     * @param path  must not be {@literal null}
     * @param value matching value
     * @return {@literal null} if {@code value} is null
     */
    protected static <T> Predicate ne(@NonNull SimpleExpression<T> path, T value) {
        return optional(value, path::ne);
    }

    /**
     * Generates 'in' predicate.
     *
     * @param path   must not be {@literal null}
     * @param values matching values
     * @return {@literal null} if {@code values} is null or empty
     */
    protected static <T> Predicate in(
            @NonNull SimpleExpression<T> path,
            Collection<? extends T> values) {
        if (isEmpty(values)) {
            return null;
        } else {
            return path.in(values);
        }
    }

    /**
     * Generates 'contains ignore case' predicate
     *
     * @param path  must not be {@literal null}
     * @param value containing keyword
     * @return {@literal null} if {@code value} is null
     */
    protected static <T extends CharSequence> Predicate containsIgnoreCase(
            @NonNull StringExpression path,
            String value) {
        return optional(value, path::containsIgnoreCase);
    }

    /**
     * Generates 'contains' predicate
     *
     * @param path  must not be {@literal null}
     * @param value containing keyword
     * @return {@literal null} if {@code value} is null
     */
    protected static <T extends CharSequence> Predicate contains(
            @NonNull StringExpression path,
            String value) {
        return optional(value, path::contains);
    }

    /**
     * Generates 'contains' predicate for collection
     *
     * @param path  must not be {@literal null}
     * @param value containing value
     * @return {@literal null} if {@code value} is null
     */
    protected static <T, Q extends SimpleExpression<? super T>> Predicate contains(
            @NonNull CollectionPathBase<? extends Collection<T>, T, Q> path, T value) {
        return optional(value, path::contains);
    }

    /**
     * Generates 'greater than (&gt;)' predicate
     *
     * @param path  must not be {@literal null}
     * @param value to compare
     * @return {@literal null} if {@code value} is null
     */
    protected static <T extends Number & Comparable<T>> Predicate gt(@NonNull NumberPath<T> path,
                                                                     T value) {
        return optional(value, path::gt);
    }

    /**
     * Generates 'greater or equal (&gt;=)' predicate
     *
     * @param path  must not be {@literal null}
     * @param value to compare
     * @return {@literal null} if {@code value} is null
     */
    protected static <T extends Number & Comparable<T>> Predicate goe(
            @NonNull NumberPath<T> path,
            T value) {
        return optional(value, path::goe);
    }

    protected static <T extends Comparable<T> & Temporal> Predicate goe(
            @NonNull TemporalExpression<T> path,
            T value) {
        return optional(value, path::goe);
    }

    /**
     * Generates 'less than (&lt;)' predicate
     *
     * @param path  must not be {@literal null}
     * @param value to compare
     * @return {@literal null} if {@code value} is null
     */
    protected static <T extends Number & Comparable<T>> Predicate lt(
            @NonNull NumberPath<T> path,
            T value) {
        return optional(value, path::lt);
    }

    /**
     * Generates 'less or equal (&lt;=)' predicate
     *
     * @param path  must not be {@literal null}
     * @param value to compare
     * @return {@literal null} if {@code value} is null
     */
    protected static <T extends Number & Comparable<T>> Predicate loe(
            @NonNull NumberPath<T> path,
            T value) {
        return optional(value, path::loe);
    }

    protected static <T extends Comparable<T> & Temporal> Predicate loe(
            @NonNull TemporalExpression<T> path,
            T value) {
        return optional(value, path::loe);
    }

    /**
     * Generates date/time comparison (&gt;) predicate
     *
     * @param path  must not be {@literal null}
     * @param value {@link java.util.Date} or Java8 DateTime
     * @return {@literal null} if {@code value} is null
     * @see java.util.Date
     * @see Temporal
     */
    protected static <T extends Comparable<T> & Temporal> Predicate after(
            @NonNull TemporalExpression<T> path,
            T value) {
        return optional(value, path::after);
    }

    /**
     * Generates date/time comparison (&lt;) predicate
     *
     * @param path  must not be {@literal null}
     * @param value {@link java.util.Date} or Java8 DateTime
     * @return {@literal null} if {@code value} is null
     * @see java.util.Date
     * @see Temporal
     */
    protected static <T extends Comparable<T> & Temporal> Predicate before(
            @NonNull TemporalExpression<T> path,
            T value) {
        return optional(value, path::before);
    }

    protected static Predicate before(DatePath<LocalDate> path, LocalDate value) {
        return optional(value, path::after);
    }

    protected static Predicate after(DatePath<LocalDate> path, LocalDate value) {
        return optional(value, path::before);

    }

    /**
     * Single value to {@link Set}
     *
     * @return {@literal null} if value is {@literal null} or
     * transformed value is {@literal null}.
     */
    protected static <I, O> Set<O> valueToSet(I value, @NonNull Function<I, O> map) {
        return ofNullable(value)
                .map(map)
                .map(ImmutableSet::of)
                .orElse(null);
    }

    /**
     * Single value to {@link Set}
     *
     * @return {@literal null} if value is {@literal null}.
     */
    protected static <T> Set<T> valueToSet(T value) {
        return valueToSet(value, Function.identity());
    }

    /**
     * Collection to {@link Set}
     *
     * @return {@literal null} if the {@code values} or transformed value is
     * null or empty.
     */
    protected static <I, O> Set<O> toSet(
            Collection<? extends I> values,
            @NonNull Function<I, O> map) {
        if (isEmpty(values)) {
            return null;
        }
        Set<O> newValues = values.stream()
                .filter(Objects::nonNull)
                .map(map)
                .filter(Objects::nonNull)
                .collect(Collectors.toSet());
        if (newValues.isEmpty()) {
            return null;
        } else {
            return newValues;
        }
    }

    /**
     * Collection to {@link Set}
     *
     * @return {@literal null} if the {@code values} is null or empty.
     */
    protected static <T> Set<T> toSet(Collection<? extends T> values) {
        return toSet(values, Function.identity());
    }

    /**
     * Builds predicates.
     *
     * @return All of criteria matching predicate
     */
    public final Predicate build() {
        PredicateBuilder builder = new PredicateBuilder();
        build(builder);
        return builder.build();
    }

    protected abstract void build(PredicateBuilder predicates);

    protected static class PredicateBuilder {
        private final List<Predicate> andPredicates = newArrayList();
        private final List<Predicate> orPredicates = newArrayList();

        public PredicateBuilder and(Predicate predicate) {
            ofNullable(predicate).ifPresent(andPredicates::add);
            return this;
        }

        public PredicateBuilder or(Predicate predicate) {
            ofNullable(predicate).ifPresent(orPredicates::add);
            return this;
        }

        public Predicate build() {
            Predicate andPredicate = ExpressionUtils.allOf(andPredicates);
            Predicate orPredicate = ExpressionUtils.anyOf(orPredicates);
            return ExpressionUtils.and(andPredicate, orPredicate);
        }


    }

}
