package com.itechgenie.apps.jdk11.sb3.configs;

import java.util.Arrays;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisClusterConfiguration;
import org.springframework.data.redis.connection.RedisPassword;
import org.springframework.data.redis.connection.jedis.JedisClientConfiguration;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.jsontype.BasicPolymorphicTypeValidator;
import com.fasterxml.jackson.databind.jsontype.PolymorphicTypeValidator;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Configuration
@ConditionalOnProperty(name = "redis.enabled", havingValue = "true", matchIfMissing = true)
@ConfigurationProperties(prefix = "spring.data.redis.cluster")
@Data
public class AppRedisConfig {

	// @Value("${password:}")
	private String password;

	// @Value("${nodes:}")
	private String[] nodes;

	// @Value("${cluster.max-redirects:3}")
	private int maxRedirects;

	@Bean
	JedisConnectionFactory jedisConnectionFactory() {

		log.info("Starting Redis cluster config with nodes list: " + nodes);

		RedisClusterConfiguration clusterConfig = new RedisClusterConfiguration(Arrays.asList(nodes));
		clusterConfig.setPassword(RedisPassword.of(password));
		clusterConfig.setMaxRedirects(maxRedirects);

		JedisClientConfiguration clientConfig = JedisClientConfiguration.builder().usePooling().build();

		return new JedisConnectionFactory(clusterConfig, clientConfig);

	}

	@Bean
	RedisTemplate<String, Object> redisTemplate() {
		Jackson2JsonRedisSerializer<Object> serializer = new Jackson2JsonRedisSerializer<Object>(Object.class);

		ObjectMapper objectMapper = new ObjectMapper();
		objectMapper.setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.ANY);

		PolymorphicTypeValidator ptv = BasicPolymorphicTypeValidator.builder().build();
		objectMapper.activateDefaultTyping(ptv);
		objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

		// serializer.setObjectMapper(objectMapper);

		RedisTemplate<String, Object> redisTemplate = new RedisTemplate<>();
		redisTemplate.setConnectionFactory(jedisConnectionFactory());
		redisTemplate.setKeySerializer(new StringRedisSerializer());
		// redisTemplate.setValueSerializer(new StringRedisSerializer());
		redisTemplate.setValueSerializer(serializer);
		redisTemplate.setHashKeySerializer(new StringRedisSerializer());
		redisTemplate.setHashValueSerializer(serializer);
		return redisTemplate;
	}

}
