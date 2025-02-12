package com.palbang.unsemawang.chat.interceptor;

import java.util.Map;

import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.http.server.ServletServerHttpRequest;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.server.HandshakeInterceptor;

import com.palbang.unsemawang.jwt.JWTUtil;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class WebSocketAuthInterceptor implements HandshakeInterceptor {

	private final JWTUtil jwtUtil;

	public WebSocketAuthInterceptor(JWTUtil jwtUtil) {
		this.jwtUtil = jwtUtil;
	}

	@Override
	public boolean beforeHandshake(ServerHttpRequest request, ServerHttpResponse response,
		WebSocketHandler wsHandler, Map<String, Object> attributes) {
		if (request instanceof ServletServerHttpRequest servletRequest) {
			HttpServletRequest httpRequest = servletRequest.getServletRequest();
			String token = extractTokenFromCookies(httpRequest);

			if (token != null && jwtUtil.isExpired(token) == false) {
				String userId = jwtUtil.getId(token);
				attributes.put("userId", userId); // WebSocket 세션에 사용자 ID 저장
				log.info("✅ WebSocket session set userId: {}", userId); // 추가 로그
				return true;
			}
		}
		log.warn("⚠️ WebSocket authentication failed!");
		return false; // 인증 실패 시 WebSocket 연결 차단
	}

	@Override
	public void afterHandshake(ServerHttpRequest request, ServerHttpResponse response,
		WebSocketHandler wsHandler, Exception exception) {
		if (exception != null) {
			log.error("WebSocket Handshake 실패: {}", exception.getMessage());
		} else {
			log.info("WebSocket Handshake 성공");
		}
	}

	private String extractTokenFromCookies(HttpServletRequest request) {
		Cookie[] cookies = request.getCookies();
		if (cookies != null) {
			for (Cookie cookie : cookies) {
				if ("Authorization".equals(cookie.getName())) {
					return cookie.getValue();
				}
			}
		}
		return null;
	}
}
