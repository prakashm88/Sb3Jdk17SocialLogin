package com.itechgenie.apps.jdk11.sb3.services;

import org.springframework.boot.actuate.audit.AuditEvent;

public interface ItgRedisCacheService {

	void storeData(String key, Object value);

	<T> T retrieveData(String key, Class<T> responseType);

	public void addEvent(AuditEvent fakeAuditEvent);

}
