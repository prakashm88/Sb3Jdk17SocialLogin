package com.itechgenie.apps.jdk11.sb3.services.impl;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Arrays;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.service.annotation.DeleteExchange;
import org.springframework.web.service.annotation.GetExchange;
import org.springframework.web.service.annotation.PostExchange;
import org.springframework.web.service.annotation.PutExchange;

import com.itechgenie.apps.jdk11.sb3.annotations.ItgWebClient;

import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Component
@Slf4j
public class ItgWebClientImpl {

	WebClient webClient = WebClient.builder().baseUrl("https://jsonplaceholder.typicode.com")
			.defaultCookie("cookie-name", "cookie-value")
			.defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE).build();

	public <T> T execute(String id, String classname, Method method, ItgWebClient annotation, Object... args) {
		log.info("Inside ItgWebClientImpl.execute: id: " + id + " - " + classname + " - methodName: " + method.getName()
				+ " - attributes: " + annotation);

		Annotation[] annotations = method.getDeclaredAnnotations();
		String _url = extractUrl(annotations);
		HttpMethod _httpMethod = extractHttpMethod(annotations);
		MultiValueMap<String, String> headersMap = extractHeaders(annotation);

		log.info("_url: " + _url + " - headersMap: " + headersMap);

		WebClient.RequestBodySpec requestSpec = webClient.method(_httpMethod).uri(_url)
				.headers(headers -> headers.addAll(headersMap));

		if (args != null && args.length > 0) {
			requestSpec.body(BodyInserters.fromValue(args[0]));
		}

		WebClient.ResponseSpec responseSpec = requestSpec.retrieve();
		Type returnType = method.getGenericReturnType();
		Class<?> elementType = extractElementType(returnType);

		log.info("Return type of the method: " + returnType);
		log.info("Element type of the method: " + returnType);

		if (Flux.class.isAssignableFrom(elementType)) {
			return (T) responseSpec.bodyToFlux(elementType);
		} else if (Mono.class.isAssignableFrom(elementType)) {
			return (T) responseSpec.bodyToMono(elementType);
		} else if (ResponseEntity.class.isAssignableFrom(elementType)) {
			return (T) responseSpec.toEntity(elementType);
		} else if (isSimpleClass(elementType)) {
			return (T) responseSpec.toEntity(elementType);

			// return (T) responseSpec.toEntity(returnType.getClass());
		} else {
			throw new IllegalArgumentException("Unsupported return type: " + returnType);
		}
	}

	private boolean isSimpleClass(Class<?> clazz) {
		return !clazz.isArray() && !Flux.class.isAssignableFrom(clazz) && !Mono.class.isAssignableFrom(clazz)
				&& !ResponseEntity.class.isAssignableFrom(clazz);
	}

	private Class<?> extractElementType(Type type) {
		if (type instanceof ParameterizedType) {
			ParameterizedType parameterizedType = (ParameterizedType) type;
			Type rawType = parameterizedType.getRawType();
			Type[] actualTypeArguments = parameterizedType.getActualTypeArguments();

			if (rawType instanceof Class<?>) {
				Class<?> rawClass = (Class<?>) rawType;
				if (Flux.class.isAssignableFrom(rawClass) || Mono.class.isAssignableFrom(rawClass)) {
					if (actualTypeArguments.length == 1) {
						Type argumentType = actualTypeArguments[0];
						if (argumentType instanceof ParameterizedType) {
							return (Class<?>) ((ParameterizedType) argumentType).getRawType();
						} else if (argumentType instanceof Class<?>) {
							return (Class<?>) argumentType;
						}
					}
				} else {
					return rawClass;
				}
			}
		} else if (type instanceof Class<?>) {
			return (Class<?>) type;
		}

		throw new IllegalArgumentException("Cannot extract element type from: " + type);
	}

	private Class<?> __extractElementType(Type type) {
		if (type instanceof ParameterizedType) {
			ParameterizedType parameterizedType = (ParameterizedType) type;
			Type rawType = parameterizedType.getRawType();
			Type[] actualTypeArguments = parameterizedType.getActualTypeArguments();

			if (rawType instanceof Class<?>) {
				Class<?> rawClass = (Class<?>) rawType;
				if (Flux.class.isAssignableFrom(rawClass) || Mono.class.isAssignableFrom(rawClass)) {
					if (actualTypeArguments.length == 1) {
						Type argumentType = actualTypeArguments[0];
						if (argumentType instanceof ParameterizedType) {
							return (Class<?>) ((ParameterizedType) argumentType).getRawType();
						} else if (argumentType instanceof Class<?>) {
							return (Class<?>) argumentType;
						}
					}
				}
			}
		}

		throw new IllegalArgumentException("Cannot extract element type from: " + type);
	}

	private HttpMethod extractHttpMethod(Annotation[] annotations) {
		for (Annotation annotation : annotations) {
			if (annotation instanceof GetExchange) {
				return HttpMethod.GET;
			} else if (annotation instanceof PostExchange) {
				return HttpMethod.POST;
			} else if (annotation instanceof PutExchange) {
				return HttpMethod.PUT;
			} else if (annotation instanceof DeleteExchange) {
				return HttpMethod.DELETE;
			}
		}

		throw new IllegalArgumentException("No HTTP method annotation found");
	}

	private String extractUrl(Annotation[] annotations) {
		for (Annotation annotation : annotations) {
			if (annotation instanceof GetExchange) {
				return ((GetExchange) annotation).url();
			} else if (annotation instanceof PostExchange) {
				return ((PostExchange) annotation).url();
			} else if (annotation instanceof PutExchange) {
				return ((PutExchange) annotation).url();
			} else if (annotation instanceof DeleteExchange) {
				return ((DeleteExchange) annotation).url();
			}
		}

		log.warn("No Url details found in annotation, treating it as an empty url");
		return "";
	}

	private MultiValueMap<String, String> extractHeaders(ItgWebClient annotation) {
		MultiValueMap<String, String> headersMap = new LinkedMultiValueMap<>();
		String[] headersArr = annotation.headers();
		Arrays.asList(headersArr).forEach((value) -> {
			if (value != null) {
				String[] _headers = value.split(":");
				if (_headers[0] != null && _headers[1] != null) {
					headersMap.add(_headers[0], _headers[1].trim());
				}

			}
		});
		return headersMap;
	}
}