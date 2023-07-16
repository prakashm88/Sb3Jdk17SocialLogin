package com.itechgenie.apps.jdk11.sb3.controllers;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/public")
public class PublicHealthController {

	@GetMapping(value = "/api/health")
	public String health() throws Exception {
		log.debug("Inside health controller");
		return "OK";
	}
}
