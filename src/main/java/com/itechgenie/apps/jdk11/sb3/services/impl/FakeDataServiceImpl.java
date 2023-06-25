package com.itechgenie.apps.jdk11.sb3.services.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.itechgenie.apps.jdk11.sb3.clients.FakeBlogServiceClient;
import com.itechgenie.apps.jdk11.sb3.clients.FakeUserServiceClient;
import com.itechgenie.apps.jdk11.sb3.clients.FakeWebClient;
import com.itechgenie.apps.jdk11.sb3.dtos.FakeBlogDTO;
import com.itechgenie.apps.jdk11.sb3.dtos.FakeUserDTO;
import com.itechgenie.apps.jdk11.sb3.services.ItgRedisCacheService;

import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;

@Service
@Slf4j
public class FakeDataServiceImpl {

	@Autowired
	ItgRedisCacheService cacheService;

	// @Autowired
	// FakeWebClient fakeWebClient;

	@Autowired
	@Lazy
	// @Qualifier("fakeBlogServiceClient")
	private FakeBlogServiceClient fakeBlogServiceClient;

	@Autowired
	@Lazy
	private FakeUserServiceClient fakeUserServiceClient;

	public FakeUserDTO getUserById(String id) {

		log.info("Inside getUserById: " + id);

		Mono<FakeUserDTO> fakeUserDTO = fakeUserServiceClient.getById(id);

		cacheService.storeData("fakeUserDTO:" + id, fakeUserDTO);

		return fakeUserDTO.block();
	}

	public List<FakeUserDTO> getUsers() {
		log.info("Inside getUsers: ");

		Map<String, Object> headers = new HashMap<>();
		headers.put("x-canary-env", true);

		FakeUserDTO dt = new FakeUserDTO("1");

		Mono<List<FakeUserDTO>> resp = fakeUserServiceClient.getAllUsers();

		log.info("Injected bean execution here::" + resp);

		return resp.block();
	}

	public List<FakeBlogDTO> getAllBlogs() {
		log.info("Inside getUsers: ");

		Map<String, Object> headers = new HashMap<>();
		headers.put("x-canary-env", true);

		List<FakeBlogDTO> resp = fakeBlogServiceClient.getBlogs(headers);

		log.info("Injected bean execution here::" + resp);

		return resp;
	}

	public Mono<FakeBlogDTO> getAllBlog(String blogId) {
		log.info("Inside getAllBlog1: ");

		Map<String, Object> headers = new HashMap<>();
		headers.put("x-canary-env", true);

		Mono<FakeBlogDTO> resp = fakeBlogServiceClient.getBlogsById(headers);

		log.info("Injected bean execution here::" + resp);

		return resp;
	}

}
