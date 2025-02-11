package com.palbang.unsemawang.common.util.redis;

import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class RedisHealthCheckService {

	/**
	 * Redis Health check를 위한 클래스
	 * 필요 없어지면 삭제해주셔도 무방합니다
	 */

	private final StringRedisTemplate redisTemplate;

	public String setAndGetValue(String key, String value) {
		// 값을 Redis에 저장
		redisTemplate.opsForValue().set(key, value);

		// Redis에서 값을 가져옴
		return redisTemplate.opsForValue().get(key);
	}

}
