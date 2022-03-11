package com.televisory.capitalmarket.config;

import org.apache.log4j.Logger;
import org.ehcache.event.CacheEvent;
import org.ehcache.event.CacheEventListener;

public class CacheEventLogger implements CacheEventListener<Object, Object> {

	Logger _log = Logger.getLogger(CacheEventLogger.class);

	@Override
	public void onEvent(CacheEvent<? extends Object, ? extends Object> cacheEvent) {
		_log.info("Cache Event Type: " + cacheEvent.getType() +", Cache key: " + cacheEvent.getKey().getClass().getName());
	}

}
