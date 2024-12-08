package com.itechgenie.apps.jdk11.sb3.dtos;

import java.time.Instant;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class FakeAuditEvent {
	
	private Integer id;
	private String name;
	private String principal;
	private Instant instant;

}
