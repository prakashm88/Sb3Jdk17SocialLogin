package com.itechgenie.apps.jdk11.sb3.controllers;

import java.net.InetAddress;
import java.sql.Date;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpCookie;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.itechgenie.apps.jdk11.sb3.dtos.FakeUserDTO;

import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;

@Slf4j
@RestController
@RequestMapping("/api")
// https://nickolasfisher.com/blog/How-to-Forward-Request-Headers-to-Downstream-Services-in-Spring-Boot-Webflux
public class AppRestController {

	@Autowired
	ObjectMapper objMapper;

	@GetMapping(value = "/user", produces = MediaType.APPLICATION_JSON_VALUE)
	public Map<String, Object> user(@AuthenticationPrincipal OAuth2User principal) throws Exception {
		if (principal != null) {
			log.info(String.valueOf(principal));
			Map<String, Object> map = Collections.singletonMap("principal", principal);
			log.info("Obtained map: " + map);
			return map;
		}
		throw new Exception("Unauthorised exception !");
	}

	@GetMapping("/env/headers")
	public ResponseEntity<Map<String, Object>> getHeaders(@RequestParam(value = "env", defaultValue = "NonePassed") String env,
			 ServerHttpRequest request) {

		Map<String, Object> returnMap = new HashMap<String, Object>();

		Date sTime;
		String sDtime;

		try {
			String hostName = InetAddress.getLocalHost().getHostName() + " with IP="
					+ InetAddress.getLocalHost().getHostAddress() + " ";
			returnMap.put("hostName", hostName);

			sTime = new Date(System.currentTimeMillis());
			sDtime = sTime.toString();

			returnMap.put("dateTome", sDtime);
			returnMap.put("serverName", request.getRemoteAddress());
			returnMap.put("requestUrl", request.getURI());
			returnMap.put("requestPath", request.getPath());
			returnMap.put("requestMethod", request.getMethod());

			try {
				MultiValueMap<String, String> headerMap = new LinkedMultiValueMap<>();
				request.getHeaders().forEach((key, value) -> headerMap.put(key, value));

				returnMap.put("headerMap", headerMap);

			} catch (Exception e) {
				log.error("Exception obtained : " + e.getMessage(), e);
				returnMap.put("headerMap", e.getMessage());
			}

			try {
				MultiValueMap<String, HttpCookie> cookieMap = request.getCookies();
				returnMap.put("headerMap", cookieMap);
				returnMap.put("cookieMap", cookieMap);
			} catch (Exception e) {
				log.error("Exception obtained : " + e.getMessage(), e);
				returnMap.put("cookieMap", e.getMessage());
			}

		} catch (Exception e) {
			returnMap.put("exception", e.getMessage());
		}
		HttpStatus status = HttpStatus.OK;

		return ResponseEntity.status(status).body(returnMap);
	}

}
