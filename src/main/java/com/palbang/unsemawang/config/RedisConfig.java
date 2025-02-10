package com.palbang.unsemawang.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.ListOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.data.redis.serializer.StringRedisSerializer;

@Configuration
public class RedisConfig {
	// @Value("${spring.data.redis.host}")
	// public String host;
	//
	// @Value("${spring.data.redis.port}")
	// public int port;

	/**
	 * Redis 연결을 위한 Connection 생성
	 */
	@Bean
	public RedisConnectionFactory redisConnectionFactory() {
		// RedisConnectionFactory: Redis 커넥션을 생성하고 관리
		// return new LettuceConnectionFactory(host, port);
		return new LettuceConnectionFactory();
	}

	/**
	 * Redis 데이터 처리를 위한 템플릿 구성
	 */
	@Bean
	public RedisTemplate<String, Object> redisTemplate() {
		RedisTemplate<String, Object> redisTemplate = new RedisTemplate<>();

		// Redis 연결
		redisTemplate.setConnectionFactory(redisConnectionFactory());

		// 일반적인 key-value일 경우
		redisTemplate.setKeySerializer(new StringRedisSerializer());
		redisTemplate.setValueSerializer(new StringRedisSerializer());

		// Hash key-value 일 경우
		redisTemplate.setHashKeySerializer(new StringRedisSerializer());
		redisTemplate.setHashValueSerializer(new StringRedisSerializer());

		// 모든 경우
		redisTemplate.setDefaultSerializer(new StringRedisSerializer());

		return redisTemplate;
	}

	/**
	 * List 데이터에 접근해 다양한 연산을 수행할 수 있는 객체 반환
	 */
	@Bean
	public ListOperations<String, Object> redisListOperations() {
		return this.redisTemplate().opsForList();
	}

	/**
	 * 단일 데이터에 접근해 다양한 연산을 수행할 수 있는 객체 반환
	 */
	@Bean
	public ValueOperations<String, Object> redisValueOperations() {
		return this.redisTemplate().opsForValue();
	}

}
