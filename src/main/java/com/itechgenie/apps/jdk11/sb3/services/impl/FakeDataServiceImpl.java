package com.itechgenie.apps.jdk11.sb3.services.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import com.itechgenie.apps.jdk11.sb3.clients.FakeBlogServiceClient;
import com.itechgenie.apps.jdk11.sb3.clients.FakeUserServiceClient;
import com.itechgenie.apps.jdk11.sb3.clients.FakeWebClient;
import com.itechgenie.apps.jdk11.sb3.dtos.FakeBlogDTO;
import com.itechgenie.apps.jdk11.sb3.dtos.FakeUserDTO;
import com.itechgenie.apps.jdk11.sb3.services.ItgRedisCacheService;

import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
@Slf4j
public class FakeDataServiceImpl {

	@Autowired
	ItgRedisCacheService cacheService;

	@Autowired
	FakeWebClient fakeWebClient;

	@Autowired
	@Lazy
	// @Qualifier("fakeBlogServiceClient")
	private FakeBlogServiceClient fakeBlogServiceClient;

	@Autowired
	@Lazy
	private FakeUserServiceClient fakeUserServiceClient;

	// Users services starts here
	public FakeUserDTO getUserById(String id) {

		log.info("Inside getUserById: " + id);

		Mono<FakeUserDTO> fakeUserDTO = fakeUserServiceClient.getById(id);

		cacheService.storeData("fakeUserDTO:" + id, fakeUserDTO);

		return fakeUserDTO.block();
	}
	
	public FakeUserDTO getFirstUser() {

		log.info("Inside getFirstUser: ");

		Mono<FakeUserDTO> fakeUserDTO = fakeUserServiceClient.getFirstUser();

		cacheService.storeData("fakeUserDTO:", fakeUserDTO);

		return fakeUserDTO.block();
	}
	
	public FakeUserDTO addUser(FakeUserDTO fakeUserDTO) {

		log.info("Inside addUser: " + fakeUserDTO);

		Mono<FakeUserDTO> fakeUserDTOReturned = fakeUserServiceClient.addUser(fakeUserDTO);

		cacheService.storeData("fakeUserDTOReturned:", fakeUserDTOReturned);

		return fakeUserDTOReturned.block();
	}

	public Flux<FakeUserDTO> getUsersProxy() {
		log.info("Inside getUsers: ");

		Map<String, Object> headers = new HashMap<>();
		headers.put("x-canary-env", true);

		Flux<FakeUserDTO> resp1 = fakeUserServiceClient.getUsers();

		log.info("Response fakeUserServiceClient getUsers via proxy::" + resp1);
		
		return resp1;
	}
	
	public Flux<FakeUserDTO> getUsersDirect() {
		log.info("Inside getUsers: ");

		Map<String, Object> headers = new HashMap<>();
		headers.put("x-canary-env", true);

		Flux<FakeUserDTO> resp2 = fakeWebClient.getFluxUsers();

		log.info("Response fakeWebClient getUsers via direct call::" + resp2);
		return resp2;
	}
	
	public Map<String, List<FakeUserDTO>> getAllUsers() {
		log.info("Inside getUsers: ");

		Map<String, Object> headers = new HashMap<>();
		headers.put("x-canary-env", true);

		Flux<FakeUserDTO> resp1 = fakeUserServiceClient.getUsers();
		Flux<FakeUserDTO> resp2 = fakeWebClient.getFluxUsers();

		log.info("Response fakeWebClient getUsers::" + resp2);
		log.info("Response fakeUserServiceClient getUsers::" + resp1);
		
		
		List<FakeUserDTO> resp1list = resp1.collectList().block();
		List<FakeUserDTO> resp2list = resp2.collectList().block();
		
		Map<String, List<FakeUserDTO>> respMap = new HashMap<>();
		respMap.put("direct", resp2list);
		respMap.put("proxy", resp1list);
		try {
			log.info("Setting up data inside cache! ");
			cacheService.storeData("ALL_USERS", respMap);
			log.info("Setting up data inside cache!: -- done ");
		} catch (Exception e) {
			log.error("Exception in storing data in redis: " + e.getMessage(), e);
		}
		
		
		log.info("Response combined getUsers::" + respMap);
		
		return respMap;
	}
	
	public Mono<Map<String, List<FakeUserDTO>>> getAllUsersNonBlocking() {
		log.info("Inside getUsers: ");

		Map<String, Object> headers = new HashMap<>();
		headers.put("x-canary-env", true);

		Flux<FakeUserDTO> resp1 = fakeUserServiceClient.getUsers();
		Flux<FakeUserDTO> resp2 = fakeWebClient.getFluxUsers();

		log.info("Response fakeWebClient getUsers::" + resp2);
		log.info("Response fakeUserServiceClient getUsers::" + resp1);
		
		
		Mono< Map<String, List<FakeUserDTO>>> resultMap = Flux.zip(resp1.collectList(), resp2.collectList())
        .map(tuple -> {
            Map<String, List<FakeUserDTO>> _resultMap = new HashMap<>();
            _resultMap.put("resp1", tuple.getT1());
            _resultMap.put("resp2", tuple.getT2());
            return _resultMap;
        })
        .single();
		
		try {
			log.info("Setting up data inside cache! ");
			cacheService.storeData("ALL_USERS", resultMap);
			log.info("Setting up data inside cache!: -- done ");
		} catch (Exception e) {
			log.error("Exception in storing data in redis: " + e.getMessage(), e);
		}
		
		
		log.info("Response combined getUsers::" + resultMap);
		
		return resultMap;
	}


	// Blogs services starts here

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
