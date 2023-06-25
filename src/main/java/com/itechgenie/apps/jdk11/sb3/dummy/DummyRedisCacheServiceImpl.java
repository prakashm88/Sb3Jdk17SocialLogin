package com.itechgenie.apps.jdk11.sb3.dummy;

import java.util.HashMap;
import java.util.Map;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

import com.itechgenie.apps.jdk11.sb3.services.ItgRedisCacheService;

@Component
@ConditionalOnProperty(name = "redis.enabled", havingValue = "false")
public class DummyRedisCacheServiceImpl implements ItgRedisCacheService {

	static Map<String, Object> dummyRedisImplMap = new HashMap<>();

	@Override
	public void storeData(String key, Object value) {
		dummyRedisImplMap.put(key, value);
	}

	@SuppressWarnings("unchecked")
	@Override
	public <T> T retrieveData(String key, Class<T> responseType) {
		return (T) dummyRedisImplMap.get(key);
	}

}
