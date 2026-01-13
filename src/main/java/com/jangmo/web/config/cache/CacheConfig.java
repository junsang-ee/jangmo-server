package com.jangmo.web.config.cache;

import com.github.benmanes.caffeine.cache.Caffeine;
import com.jangmo.web.constants.cache.CacheType;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.caffeine.CaffeineCache;
import org.springframework.cache.support.SimpleCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@EnableCaching
@Configuration
public class CacheConfig {
	@Bean
	public CacheManager cacheManager() {
		SimpleCacheManager cacheManager = new SimpleCacheManager();
		List<CaffeineCache> caches = Arrays.stream(CacheType.values())
			.map(cache -> new CaffeineCache(
				cache.getName(),
				Caffeine.newBuilder().recordStats()
					.expireAfterWrite(cache.getExpiredTime(), TimeUnit.MINUTES)
					.maximumSize(cache.getMaximumSize())
					.build())
			).collect(Collectors.toList());
		cacheManager.setCaches(caches);
		return cacheManager;
	}
}
