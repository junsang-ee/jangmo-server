package com.jangmo.web.service.cache;

import com.jangmo.web.constants.cache.CacheType;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.CacheManager;
import org.springframework.cache.caffeine.CaffeineCache;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class CacheService {
    private final CacheManager cacheManager;

    public <K, V> CaffeineCache getCache(CacheType cacheType) {
        return (CaffeineCache) cacheManager.getCache(cacheType.getName());
    }

    public <K, V> V get(CacheType cacheType, K key) {
        return (V) getCache(cacheType).get(key).get();
    }

    public <K, V> void put(CacheType cacheType, K key, V value) {
        getCache(cacheType).put(key, value);
    }

    public <K> void remove(CacheType cacheType, K key) {
        getCache(cacheType).evict(key);
    }

}
