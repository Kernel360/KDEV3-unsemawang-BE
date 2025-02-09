// package com.palbang.unsemawang.chat.interceptor;
//
// import java.util.Map;
//
// import org.slf4j.Logger;
// import org.slf4j.LoggerFactory;
// import org.springframework.http.server.ServerHttpRequest;
// import org.springframework.http.server.ServerHttpResponse;
// import org.springframework.web.socket.WebSocketHandler;
// import org.springframework.web.socket.server.HandshakeInterceptor;
//
// import com.palbang.unsemawang.jwt.JWTUtil;
//
// public class WebSocketAuthInterceptor implements HandshakeInterceptor {
//
// 	private static final Logger logger = LoggerFactory.getLogger(WebSocketAuthInterceptor.class);
// 	private final JWTUtil jwtUtil;
//
// 	public WebSocketAuthInterceptor(JWTUtil jwtUtil) {
// 		this.jwtUtil = jwtUtil;
// 	}
//
// 	@Override
// 	public boolean beforeHandshake(
// 		ServerHttpRequest request,
// 		ServerHttpResponse response,
// 		WebSocketHandler wsHandler,
// 		Map<String, Object> attributes) throws Exception {
//
// 		// 우선 헤더에서 시도
// 		String authorizationHeader = request.getHeaders().getFirst("Authorization");
// 		String token = null;
// 		if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
// 			token = authorizationHeader.substring(7);
// 		}
// 		// 헤더에 없으면 URL 쿼리 파라미터에서 토큰 추출
// 		if (token == null) {
// 			String query = request.getURI().getQuery();
// 			if (query != null && query.contains("token=")) {
// 				// 단순한 방법: "token=" 뒤의 문자열을 토큰으로 간주
// 				token = query.split("token=")[1];
// 			}
// 		}
//
// 		if (token == null || jwtUtil.isExpired(token)) {
// 			logger.warn("Missing or invalid JWT token.");
// 			return false; // 연결 거부
// 		}
//
// 		String userId = jwtUtil.getId(token);
// 		attributes.put("userId", userId);
// 		logger.info("WebSocket authorized for userId: {}", userId);
// 		return true;
// 	}
//
// 	@Override
// 	public void afterHandshake(
// 		ServerHttpRequest request,
// 		ServerHttpResponse response,
// 		WebSocketHandler wsHandler,
// 		Exception exception) {
// 		// After Handshake Handling (Optional)
// 	}
// }