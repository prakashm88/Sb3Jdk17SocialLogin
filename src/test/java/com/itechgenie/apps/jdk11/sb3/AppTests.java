package com.itechgenie.apps.jdk11.sb3;

import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.util.Arrays;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpMethod;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import com.itechgenie.apps.jdk11.sb3.clients.FakeUserServiceClient;
import com.itechgenie.apps.jdk11.sb3.configs.AppWebClientConfig;
import com.itechgenie.apps.jdk11.sb3.dtos.FakeUserDTO;
import com.itechgenie.apps.jdk11.sb3.services.impl.ItgWebClientImpl;

import reactor.core.publisher.Flux;

@SpringBootTest
public class AppTests {

	@Test
	public void contextLoads() throws NoSuchMethodException, SecurityException {

		
		AppWebClientConfig appWebClientConfig = new AppWebClientConfig();
		appWebClientConfig.setDebug(true);
		
		
		ItgWebClientImpl itgWebClientImpl = new ItgWebClientImpl(appWebClientConfig);
		itgWebClientImpl.loadConfigs();
		
		MultiValueMap<String, String> headersMap = new LinkedMultiValueMap<>();

		// Class<Flux<FakeUserDTO>> returnType = (Class<Flux<FakeUserDTO>>) (Class<?>)
		// Flux.class;

		// Type elementType = returnType.getClass();

		String[] headersArr = { "Content-Type: application/json" };
		Arrays.asList(headersArr).forEach((value) -> {
			if (value != null) {
				String[] _headers = value.split(":");
				if (_headers[0] != null && _headers[1] != null) {
					headersMap.add(_headers[0], _headers[1].trim());
				}
			}
		});

		Method method = FakeUserServiceClient.class.getMethod("getUsers", null);

		Type elementType = method.getGenericReturnType();
		Class<?> returnType = itgWebClientImpl.extractElementType(elementType);

		Flux<FakeUserDTO> resp = (Flux<FakeUserDTO>) itgWebClientImpl.executeWebClient("testrest", "/users", HttpMethod.GET, headersMap,
				null, elementType, returnType);

		System.out.println("First api response !! " + resp.blockFirst());
		
	/*	Method method2 = FakeUserServiceClient.class.getMethod("getById", String.class);

		Type elementType2 = method2.getGenericReturnType();
		Class<?> returnType2 = itgWebClientImpl.extractElementType(elementType);

		Flux<FakeUserDTO> resp2 = (Flux<FakeUserDTO>) itgWebClientImpl.executeWebClient("testrest", "/users/{id}", HttpMethod.GET, headersMap,
				null, elementType2, returnType2);
		
		System.out.println("Second api response !! " + resp2.blockFirst());  */
	}
}
