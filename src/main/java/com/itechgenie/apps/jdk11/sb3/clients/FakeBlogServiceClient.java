package com.itechgenie.apps.jdk11.sb3.clients;

import java.util.List;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.service.annotation.GetExchange;

import com.itechgenie.apps.jdk11.sb3.annotations.ItgWebClient;
import com.itechgenie.apps.jdk11.sb3.dtos.FakeBlogDTO;

import reactor.core.publisher.Mono;

//https://randomuser.me/documentation#howto
@ItgWebClient(id = "fakeBlogServiceClient", url = "https://jsonplaceholder.typicode.com", headers = {
		"Content-Type: application/json" })
public interface FakeBlogServiceClient {

	@GetExchange("/blogs/{id}")
	Mono<ResponseEntity<FakeBlogDTO>> getBlogById(@RequestParam("id") String id);

	@GetExchange("/blogs")
	List<FakeBlogDTO> getBlogs(@RequestHeader Map<String, Object> headers);

	@GetExchange("/blogs/1")
	Mono<FakeBlogDTO> getBlogsById(@RequestHeader Map<String, Object> headers);

}