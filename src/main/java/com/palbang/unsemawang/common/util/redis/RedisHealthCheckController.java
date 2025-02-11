package com.palbang.unsemawang.common.util.redis;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@Tag(name = "레디스")
public class RedisHealthCheckController {

	/**
	 * Redis Health check를 위한 클래스
	 * 필요 없어지면 삭제해주셔도 무방합니다
	 */

	private final RedisHealthCheckService redisService;

	@Operation(
		summary = "테스트 용도",
		description = "레디스가 살아 있으면 값을 저장하고 그 값을 가져옵니다"
	)
	@GetMapping("/health-check")
	public String testRedisConnection(@RequestParam String key, @RequestParam String value) {
		// Redis에 값을 설정하고 그 값을 가져옴
		return redisService.setAndGetValue(key, value);
	}

}
