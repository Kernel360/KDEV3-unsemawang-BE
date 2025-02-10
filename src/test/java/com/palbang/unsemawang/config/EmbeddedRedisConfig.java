package com.palbang.unsemawang.config;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.TestConfiguration;

import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import redis.embedded.RedisServer;

@TestConfiguration
public class EmbeddedRedisConfig {
	@Value("${spring.data.redis.port}")
	public int port;

	private RedisServer redisServer;

	public EmbeddedRedisConfig() throws IOException {
		this.redisServer = new RedisServer(port);
	}

	@PostConstruct
	public void startRedis() {
		this.redisServer.start();
	}

	@PreDestroy
	public void stopRedis() {
		this.redisServer.stop();
	}
}
