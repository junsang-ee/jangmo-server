package com.jangmo.web.config;

import lombok.NoArgsConstructor;

import static org.springframework.core.Ordered.HIGHEST_PRECEDENCE;
import static lombok.AccessLevel.PRIVATE;

@NoArgsConstructor(access = PRIVATE)
public final class AspectOrder {

    public static final int VALIDATION = HIGHEST_PRECEDENCE;

    public static final int METHOD_SECURITY = VALIDATION + 1000;

    public static final int OUT_TRANSACTION = METHOD_SECURITY + 1000;

    public static final int TRANSACTION = OUT_TRANSACTION + 1000;

    public static final int IN_TRANSACTION = TRANSACTION + 1000;

    public static final int CACHE = TRANSACTION + 1000;

}
