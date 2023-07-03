package com.itechgenie.apps.jdk11.sb3.clients;

import java.time.Duration;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import com.itechgenie.apps.jdk11.sb3.dtos.FakeUserDTO;

import io.netty.channel.ChannelOption;
import io.netty.handler.timeout.ReadTimeoutHandler;
import io.netty.handler.timeout.WriteTimeoutHandler;
import reactor.core.publisher.Flux;
import reactor.netty.http.client.HttpClient;

@Service
public class FakeWebClient {

	HttpClient httpClient = HttpClient.create()
			  .option(ChannelOption.CONNECT_TIMEOUT_MILLIS, 5000)
			  .responseTimeout(Duration.ofMillis(5000))
			//  .wiretap(true)
			  .doOnConnected(conn -> 
			    conn.addHandlerLast(new ReadTimeoutHandler(5000, TimeUnit.MILLISECONDS))
			      .addHandlerLast(new WriteTimeoutHandler(5000, TimeUnit.MILLISECONDS)));
	
	WebClient webClient = WebClient.builder()
			.clientConnector(new ReactorClientHttpConnector(httpClient)) // customize the timeout options here
			.baseUrl("https://jsonplaceholder.typicode.com")
			.defaultCookie("cookie-name", "cookie-value")
			.defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
			.build();

	public List<FakeUserDTO> getUsers() {
		return webClient.get().uri("/users").header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
				.retrieve().bodyToMono(new ParameterizedTypeReference<List<FakeUserDTO>>() {
				}).block(); 
	}
	
	public Flux<FakeUserDTO> getFluxUsers() {
		return webClient.get().uri("/users").header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
				.retrieve().bodyToFlux(FakeUserDTO.class);
	}

	public FakeUserDTO getUserById(String userId) {
		return webClient.get().uri("/users/" + userId)
				.header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE).retrieve()
				.bodyToMono(FakeUserDTO.class).block();
	}
}
