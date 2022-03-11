package com.televisory.capitalmarket.config;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import javax.cache.Caching;

import org.ehcache.config.CacheConfiguration;
import org.ehcache.config.builders.CacheConfigurationBuilder;
import org.ehcache.config.builders.CacheEventListenerConfigurationBuilder;
import org.ehcache.config.builders.ResourcePoolsBuilder;
import org.ehcache.config.units.MemoryUnit;
import org.ehcache.core.config.DefaultConfiguration;
import org.ehcache.event.EventType;
import org.ehcache.expiry.Duration;
import org.ehcache.expiry.Expirations;
import org.ehcache.impl.config.event.DefaultCacheEventListenerConfiguration;
import org.ehcache.impl.config.persistence.DefaultPersistenceConfiguration;
import org.ehcache.jsr107.EhcacheCachingProvider;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.jcache.JCacheCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableCaching
public class CacheConfig {

	private static final String EHCACHE_CACHING_PROVIDER = "org.ehcache.jsr107.EhcacheCachingProvider";

	@Value("${CM.CACHE.FILEPATH}")
	private String FILE_PATH;

	@Value("${CM.CACHE.DISKSIZE}")
	private long cacheDiskSize;

	@Value("${CM.CACHE.EXPIRETIME}")
	private long cacheExpireTime;

	@Value("${CM.DAYS.CACHE.EXPIRETIME}")
	private long daysCacheExpireTime;

	@Value("${CM.DAYS.CACHE.DISKSIZE}")
	private long daysCacheDiskSize;

	@Bean
	public JCacheCacheManager jCacheManager() {
		JCacheCacheManager jCacheManager = new JCacheCacheManager(cacheManager());
		return jCacheManager;
	}

	@Bean(destroyMethod = "close")
	public javax.cache.CacheManager cacheManager() {

		CacheConfiguration<Object, Object> cmHourCacheConfig = createCacheConfigurationInstance(
				createCacheEventLister(), TimeUnit.HOURS, cacheExpireTime, MemoryUnit.GB, cacheDiskSize);
		
		CacheConfiguration<Object, Object> cmDAYSCacheConfig = createCacheConfigurationInstance(
				createCacheEventLister(), TimeUnit.DAYS, daysCacheExpireTime, MemoryUnit.GB, daysCacheDiskSize);


		Map<String, CacheConfiguration<?, ?>> caches = new HashMap<>();
		caches.put("CM_CACHE", cmHourCacheConfig);
		caches.put("CM_DAYS_CACHE", cmDAYSCacheConfig);

		EhcacheCachingProvider provider = (EhcacheCachingProvider) Caching.getCachingProvider(EHCACHE_CACHING_PROVIDER);
		org.ehcache.config.Configuration configuration = new DefaultConfiguration(caches,
				provider.getDefaultClassLoader(), new DefaultPersistenceConfiguration(new File(FILE_PATH)));

		return provider.getCacheManager(provider.getDefaultURI(), (org.ehcache.config.Configuration) configuration);
	}

	private CacheConfiguration<Object, Object> createCacheConfigurationInstance(
			DefaultCacheEventListenerConfiguration cacheEventListenerConfiguration, TimeUnit expireTimeUnit,
			long cacheExpireTime, MemoryUnit diskSizeUnit, long cacheDiskSize) {
		CacheConfiguration<Object, Object> cmDailyCacheConfig = CacheConfigurationBuilder
				.newCacheConfigurationBuilder(Object.class, Object.class,
						ResourcePoolsBuilder.newResourcePoolsBuilder().disk(cacheDiskSize, diskSizeUnit).build())
				.add(cacheEventListenerConfiguration)
				.withExpiry(Expirations.timeToLiveExpiration(Duration.of(cacheExpireTime, expireTimeUnit))).build();
		return cmDailyCacheConfig;
	}

	private DefaultCacheEventListenerConfiguration createCacheEventLister() {
		DefaultCacheEventListenerConfiguration cacheEventListenerConfiguration = CacheEventListenerConfigurationBuilder
				.newEventListenerConfiguration(new CacheEventLogger(), EventType.CREATED, EventType.UPDATED,
						EventType.EXPIRED, EventType.EVICTED, EventType.REMOVED)
				.unordered().asynchronous().build();
		return cacheEventListenerConfiguration;
	}

}
