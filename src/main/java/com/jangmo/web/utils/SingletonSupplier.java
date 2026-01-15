package com.jangmo.web.utils;

import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import java.util.function.Supplier;

import static lombok.AccessLevel.PRIVATE;

@RequiredArgsConstructor(staticName = "of")
public final class SingletonSupplier<T> implements Supplier<T> {

	private @NonNull Supplier<T> supplier;

	@Getter(value = PRIVATE, lazy = true)
	private final T instance = supplier.get();

	@Override
	public T get() {
		return getInstance();
	}
}
