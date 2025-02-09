package com.palbang.unsemawang.chat.config;

import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.event.EventListener;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;
import org.springframework.web.socket.messaging.SessionConnectedEvent;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;

import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {

	private final RedisTemplate<String, String> redisTemplate;

	public WebSocketConfig(RedisTemplate<String, String> redisTemplate) {
		this.redisTemplate = redisTemplate;
	}

	@Override
	public void registerStompEndpoints(StompEndpointRegistry registry) {
		registry.addEndpoint("/ws")
			.setAllowedOriginPatterns("*")
			.withSockJS();
	}

	@EventListener
	public void handleWebSocketConnectListener(SessionConnectedEvent event) {
		StompHeaderAccessor headerAccessor = StompHeaderAccessor.wrap(event.getMessage());

		// ✅ 세션 속성이 없으면 새로 생성
		if (headerAccessor.getSessionAttributes() == null) {
			log.warn("⚠️ WebSocket 세션 속성이 존재하지 않음! 새로 생성합니다.");
			headerAccessor.setSessionAttributes(new HashMap<>()); // 새 HashMap으로 설정
		}

		// ✅ Query Parameter 또는 Native Headers에서 token 가져오기
		String token = headerAccessor.getFirstNativeHeader("Authorization");

		if (token == null) {
			log.error("❌ WebSocket 연결 시 Authorization 헤더가 없음!");
			return;
		}

		// ✅ JWT에서 userId 추출
		String userId = extractUserIdFromJwt(token);

		if (userId != null) {
			// ✅ WebSocket 세션에 userId 저장
			headerAccessor.getSessionAttributes().put("userId", userId);
			redisTemplate.opsForValue().set("online:" + userId, "true");
			log.info("✅ WebSocket 연결 성공! userId={}", userId);
		} else {
			log.error("❌ JWT에서 userId를 추출할 수 없음");
		}
	}

	@EventListener
	public void handleWebSocketDisconnectListener(SessionDisconnectEvent event) {
		StompHeaderAccessor headerAccessor = StompHeaderAccessor.wrap(event.getMessage());

		if (headerAccessor.getSessionAttributes() == null) {
			log.warn("⚠️ WebSocket 세션 속성이 존재하지 않음. 종료 프로세스 생략.");
			return;
		}

		String userId = (String)headerAccessor.getSessionAttributes().get("userId");

		if (userId != null) {
			redisTemplate.delete("online:" + userId);
			log.info("🚪 WebSocket 연결 종료: userId={}", userId);
		}
	}

	// ✅ JWT에서 userId 추출하는 메서드
	private String extractUserIdFromJwt(String jwtToken) {
		try {
			if (jwtToken != null && jwtToken.startsWith("Bearer ")) {
				String token = jwtToken.substring(7);
				String base64Url = token.split("\\.")[1];
				String base64 = base64Url.replace('-', '+').replace('_', '/');
				String decoded = new String(Base64.getDecoder().decode(base64));

				// ✅ `Jackson ObjectMapper` 사용하여 JSON 파싱
				ObjectMapper objectMapper = new ObjectMapper();
				Map<String, Object> jsonMap = objectMapper.readValue(decoded, Map.class);

				return jsonMap.get("id").toString(); // ✅ `id` 필드 추출
			} else {
				log.error("❌ JWT 토큰 형식이 올바르지 않음: {}", jwtToken);
			}
		} catch (Exception e) {
			log.error("❌ JWT 파싱 중 오류 발생: {}", e.getMessage());
		}
		return null;
	}
}
