package com.palbang.unsemawang.chat.websocket;

import org.springframework.context.event.EventListener;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.SessionConnectEvent;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@RequiredArgsConstructor
public class WebSocketEventListener {

	private final RedisTemplate<String, String> redisTemplate;

	/**
	 * 사용자가 WebSocket에 연결될 때 실행됨
	 */
	@EventListener
	public void handleWebSocketConnectListener(SessionConnectEvent event) {
		StompHeaderAccessor headerAccessor = StompHeaderAccessor.wrap(event.getMessage());
		String userId = headerAccessor.getNativeHeader("userId") != null
			? headerAccessor.getNativeHeader("userId").get(0)
			: null;

		if (userId != null) {
			log.info("✅ User connected: {}", userId);
			redisTemplate.opsForValue().set("online:" + userId, "1"); // ✅ 유저 온라인 상태 저장
			headerAccessor.getSessionAttributes().put("userId", userId); // ✅ 세션에 저장
		}
	}

	/**
	 * 사용자가 WebSocket 연결을 끊을 때 실행됨
	 */
	@EventListener
	public void handleWebSocketDisconnectListener(SessionDisconnectEvent event) {
		StompHeaderAccessor headerAccessor = StompHeaderAccessor.wrap(event.getMessage());
		String userId = (String)headerAccessor.getSessionAttributes().get("userId");

		if (userId != null) {
			log.info("❌ User disconnected: {}", userId);
			redisTemplate.delete("online:" + userId); // ✅ 유저 온라인 상태 삭제
		}
	}
}
