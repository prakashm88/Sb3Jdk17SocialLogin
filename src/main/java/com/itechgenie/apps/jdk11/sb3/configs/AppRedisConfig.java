package com.itechgenie.apps.jdk11.sb3.configs;

import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.data.redis.connection.RedisClusterConfiguration;
import org.springframework.data.redis.connection.RedisPassword;
import org.springframework.data.redis.connection.jedis.JedisClientConfiguration;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.stereotype.Component;

@Component
@ConditionalOnProperty(name = "redis.enabled", havingValue = "true", matchIfMissing = true)
@ConfigurationProperties(prefix = "spring.redis")
public class AppRedisConfig {

	@Value("${password:}")
	private String redisPassword;

	@Value("${cluster.nodes:}")
	private List<String> redisClusterNodes;

	@Value("${cluster.max-redirects:3}")
	private int redisClusterMaxRedirects;

	@Bean
	public JedisConnectionFactory jedisConnectionFactory() {

		RedisClusterConfiguration clusterConfig = new RedisClusterConfiguration(redisClusterNodes);
		clusterConfig.setPassword(RedisPassword.of(redisPassword));
		clusterConfig.setMaxRedirects(redisClusterMaxRedirects);

		JedisClientConfiguration clientConfig = JedisClientConfiguration.builder().usePooling().build();

		return new JedisConnectionFactory(clusterConfig, clientConfig);

	}

	@Bean
	public RedisTemplate<String, String> redisTemplate() {
		RedisTemplate<String, String> redisTemplate = new RedisTemplate<>();
		redisTemplate.setConnectionFactory(jedisConnectionFactory());
		redisTemplate.setKeySerializer(new StringRedisSerializer());
		redisTemplate.setValueSerializer(new StringRedisSerializer());
		return redisTemplate;
	}

}
