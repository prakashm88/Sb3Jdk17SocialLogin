package com.itechgenie.apps.jdk11.sb3.services;

public interface ItgRedisCacheService {

	void storeData(String key, Object value);

	<T> T retrieveData(String key, Class<T> responseType);

}
