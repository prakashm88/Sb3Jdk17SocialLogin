package com.itechgenie.apps.jdk11.sb3.clients;

import java.util.List;

import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import com.itechgenie.apps.jdk11.sb3.dtos.FakeUserDTO;

@Service
public class FakeWebClient {

	WebClient webClient = WebClient.create("https://jsonplaceholder.typicode.com");

	public List<FakeUserDTO> getUsers() {
		return webClient.get().uri("/users").header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
				.retrieve().bodyToMono(new ParameterizedTypeReference<List<FakeUserDTO>>() {
				}).block();
	}

	public FakeUserDTO getUserById(String userId) {
		return webClient.get().uri("/users/" + userId)
				.header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE).retrieve()
				.bodyToMono(FakeUserDTO.class).block();
	}
}
