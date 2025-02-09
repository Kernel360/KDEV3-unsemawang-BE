package com.palbang.unsemawang.chat.config;

import java.util.Base64;
import java.util.Map;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.event.EventListener;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
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
		log.info("✅ WebSocket Endpoint Registered: /ws");
	}

	@Override
	public void configureMessageBroker(MessageBrokerRegistry config) {
		config.enableSimpleBroker("/topic"); // ✅ 구독 엔드포인트
		config.setApplicationDestinationPrefixes("/app"); // ✅ 메시지 전송 경로 설정
		log.info("✅ WebSocket Message Broker Configured.");
	}

	@EventListener
	public void handleWebSocketConnectListener(SessionConnectedEvent event) {
		StompHeaderAccessor headerAccessor = StompHeaderAccessor.wrap(event.getMessage());
		String token = headerAccessor.getFirstNativeHeader("Authorization");

		if (token == null) {
			log.warn("⚠️ WebSocket 연결 시 Authorization 헤더가 없음");
			return;
		}

		String userId = extractUserIdFromJwt(token);
		if (userId != null) {
			redisTemplate.opsForValue().set("online:" + userId, "true");
			log.info("✅ WebSocket 연결 성공! userId={}", userId);
		}
	}

	@EventListener
	public void handleWebSocketDisconnectListener(SessionDisconnectEvent event) {
		StompHeaderAccessor headerAccessor = StompHeaderAccessor.wrap(event.getMessage());
		String userId = (String)headerAccessor.getSessionAttributes().get("userId");

		if (userId != null) {
			redisTemplate.delete("online:" + userId);
			log.info("🚪 WebSocket 연결 종료: userId={}", userId);
		}
	}

	private String extractUserIdFromJwt(String jwtToken) {
		try {
			if (jwtToken != null && jwtToken.startsWith("Bearer ")) {
				String token = jwtToken.substring(7);
				String base64Url = token.split("\\.")[1];
				String base64 = base64Url.replace('-', '+').replace('_', '/');
				String decoded = new String(Base64.getDecoder().decode(base64));

				ObjectMapper objectMapper = new ObjectMapper();
				Map<String, Object> jsonMap = objectMapper.readValue(decoded, Map.class);
				return jsonMap.get("id").toString();
			}
		} catch (Exception e) {
			log.error("❌ JWT 파싱 중 오류 발생: {}", e.getMessage());
		}
		return null;
	}
}
