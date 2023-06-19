package com.itechgenie.apps.jdk11.sb3.controllers;

import java.util.Collections;
import java.util.Map;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;

@Controller
@Slf4j
public class AppAuthController {
	@GetMapping("/")
	public String home() {
		return "home.html";
	}

	@GetMapping("/login")
	public String login() {
		return "loginToApp";
	}

	@GetMapping("/error")
	public String error(HttpServletRequest request) {
		String message = (String) request.getSession().getAttribute("error.message");
		request.getSession().removeAttribute("error.message");
		return message;
	}

	@GetMapping("/user")
	public Map<String, Object> user(@AuthenticationPrincipal OAuth2User principal) {
		if (principal != null) {
			log.info(String.valueOf(principal));
		}
		return Collections.singletonMap("principal", String.valueOf(principal));
	}
}
