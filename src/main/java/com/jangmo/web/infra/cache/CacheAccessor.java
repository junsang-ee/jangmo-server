package com.jangmo.web.infra.cache;

import com.jangmo.web.constants.cache.CacheType;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.stereotype.Component;

import java.util.Optional;

@RequiredArgsConstructor
@Component
public class CacheAccessor {
	private final CacheManager cacheManager;

	private Cache getCache(CacheType cacheType) {
        return cacheManager.getCache(cacheType.getName());
    }

	public <K, V> Optional<V> get(CacheType cacheType, K key, Class<V> clazz) {
		Cache.ValueWrapper wrapper = getCache(cacheType).get(key);
		if (wrapper == null) return Optional.empty();

		Object value = wrapper.get();
		if (clazz.isInstance(value)) {
			return Optional.of(clazz.cast(value));
		} else {
			return Optional.empty();
		}
	}

	public <K, V> void put(CacheType cacheType, K key, V value) {
		getCache(cacheType).put(key, value);
	}

	public <K> void remove(CacheType cacheType, K key) {
		getCache(cacheType).evict(key);
	}
}
