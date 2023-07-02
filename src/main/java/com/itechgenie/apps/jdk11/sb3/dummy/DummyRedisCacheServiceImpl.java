package com.itechgenie.apps.jdk11.sb3.dummy;

import java.util.HashMap;
import java.util.Map;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

import com.itechgenie.apps.jdk11.sb3.services.ItgRedisCacheService;

import lombok.extern.slf4j.Slf4j;

@Component
@ConditionalOnProperty(name = "redis.enabled", havingValue = "false")
@Slf4j
public class DummyRedisCacheServiceImpl implements ItgRedisCacheService {

	static Map<String, Object> dummyRedisImplMap = new HashMap<>();

	@Override
	public void storeData(String key, Object value) {
		log.debug("Key: " + key + " - value: " + value);
		dummyRedisImplMap.put(key, value);
	}

	@SuppressWarnings("unchecked")
	@Override
	public <T> T retrieveData(String key, Class<T> responseType) {
		log.debug("Key: " + key + " - responseType: " + responseType);
		return (T) dummyRedisImplMap.get(key);
	}

}
