package com.itechgenie.apps.jdk11.sb3.services.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import com.itechgenie.apps.jdk11.sb3.services.ItgRedisCacheService;

@Component
@ConditionalOnProperty(name = "redis.enabled", havingValue = "true", matchIfMissing = true)
public class ItgRedisCacheServiceImpl implements ItgRedisCacheService {

	@Autowired
	private RedisTemplate<String, Object> redisTemplate;

	public void storeData(String key, Object value) {
		redisTemplate.opsForValue().set(key, value);
	}

	public <T> T retrieveData(String key, Class<T> responseType) {
		return (T) redisTemplate.opsForValue().get(key);
	}

}
