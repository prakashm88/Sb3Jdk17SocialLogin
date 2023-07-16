package com.itechgenie.apps.jdk11.sb3.controllers;

import java.util.Collections;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/api")
public class AppRestController {

	@Autowired
	ObjectMapper objMapper ;
	
	@GetMapping(value = "/user", produces = MediaType.APPLICATION_JSON_VALUE)
	public Map<String, Object> user(@AuthenticationPrincipal OAuth2User principal) throws Exception {
		if (principal != null) {
			log.info(String.valueOf(principal));
			Map<String, Object> map = Collections.singletonMap("principal", principal );
			log.info("Obtained map: " + map);
			return map;
		}
		throw new Exception("Unauthorised exception !");
	}
}
