package com.itechgenie.apps.jdk11.sb3.clients;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.service.annotation.GetExchange;
import org.springframework.web.service.annotation.PostExchange;

import com.itechgenie.apps.jdk11.sb3.annotations.ItgWebClient;
import com.itechgenie.apps.jdk11.sb3.dtos.FakeUserDTO;

import reactor.core.publisher.Mono;

//https://randomuser.me/documentation#howto
//https://www.baeldung.com/spring-6-http-interface
@ItgWebClient(id = "fakeUserServiceClient", url = "https://jsonplaceholder.typicode.com", headers = {
		"Content-Type: application/json" })
public interface FakeUserServiceClient {

	@GetExchange("/users/{id}")
	Mono<ResponseEntity<FakeUserDTO>> getUserById(String id);

	@GetExchange("/users/{id}")
	Mono<FakeUserDTO> getById(@PathVariable("id") Long id);

	@PostExchange("/")
	Mono<ResponseEntity<List<FakeUserDTO>>> getUsers(@RequestBody FakeUserDTO user);

}