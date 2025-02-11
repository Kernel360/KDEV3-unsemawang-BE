package com.palbang.unsemawang.config;

import java.time.Duration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceClientConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;

@Configuration
public class RedisConfig {
	@Value("${spring.data.redis.host}")
	public String host;

	@Value("${spring.data.redis.port}")
	public int port;

	@Bean
	public RedisConnectionFactory redisConnectionFactory() {

		LettuceClientConfiguration clientConfiguration = LettuceClientConfiguration.builder()
			.useSsl()    // SSL 사용 설정 : mandatory to use elasti-cache
			.and()
			.commandTimeout(Duration.ofSeconds(5))
			.build();

		return new LettuceConnectionFactory(new RedisStandaloneConfiguration(host, port), clientConfiguration);
	}
}
