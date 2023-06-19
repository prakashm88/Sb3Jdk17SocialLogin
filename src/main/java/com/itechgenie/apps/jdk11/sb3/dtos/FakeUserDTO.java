package com.itechgenie.apps.jdk11.sb3.dtos;

import java.util.Map;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonIgnoreType;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@NoArgsConstructor
@ToString
@AllArgsConstructor
@JsonIgnoreType
@JsonIgnoreProperties(ignoreUnknown = true)
public class FakeUserDTO {

	public FakeUserDTO(String id) {
		this.id = id;
	}
	
	private String id;
	private String name;
	private String username;
	private String phone;
	private String website;
	private String email;
	private Map<String, String> company;
	
}
