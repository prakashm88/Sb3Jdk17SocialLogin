package com.itechgenie.apps.jdk11.sb3.repositories;

import java.time.Instant;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.actuate.audit.AuditEvent;
import org.springframework.boot.actuate.audit.AuditEventRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Repository;

import com.itechgenie.apps.jdk11.sb3.services.ItgRedisCacheService;

@Repository
@Configuration
public class AppAuditEventRepository {

	@Bean
	AuditEventRepository auditEventRepository() {
		return new AuditEventRepository() {

			@Autowired
			private ItgRedisCacheService itgRedisCacheServiceImpl;

			@Override
			public void add(AuditEvent event) {
				itgRedisCacheServiceImpl.addEvent(event);
			}

			@Override
			public List<AuditEvent> find(String principal, Instant after, String type) {
				List<AuditEvent> auditEvents = new ArrayList<>();

				Map<String, Object> dt = new HashMap<>();
				dt.put("principal", principal);
				dt.put("dt", Instant.now());
				dt.put("type", type);

				AuditEvent auditEvent = new AuditEvent(principal, "MyEVENT", dt);
				auditEvents.add(auditEvent);

				return auditEvents;
			}
		};
	}

	public void add(AuditEvent auditEvent) {
		// TODO Auto-generated method stub
		
	}

}
