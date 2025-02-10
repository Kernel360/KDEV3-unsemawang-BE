package com.palbang.unsemawang.jwt;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import com.palbang.unsemawang.oauth2.dto.CustomOAuth2User;
import com.palbang.unsemawang.oauth2.dto.UserDto;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class JWTFilter extends OncePerRequestFilter {
	private static final Logger logger = LoggerFactory.getLogger(JWTFilter.class);

	private final JWTUtil jwtUtil;

	public JWTFilter(JWTUtil jwtUtil) {
		this.jwtUtil = jwtUtil;
	}

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
		throws ServletException, IOException {

		String token = extractToken(request);

		// 토큰이 없으면 필터 진행 후 종료
		if (token == null) {
			logger.debug("JWT Token is missing.");
			filterChain.doFilter(request, response);
			return;
		}

		// 토큰 유효성 검증
		if (jwtUtil.isExpired(token)) {
			logger.debug("JWT Token is expired.");
			filterChain.doFilter(request, response);
			return;
		}

		// 토큰에서 유저 정보 추출
		String id = jwtUtil.getId(token);
		String email = jwtUtil.getEmail(token);
		String role = jwtUtil.getRole(token);
		logger.debug("Extracted JWT Info - ID: {}, Email: {}, Role: {}", id, email, role);

		// UserDto 객체 생성 및 사용자 정보 설정
		UserDto userDto = new UserDto();
		userDto.setId(id);
		userDto.setEmail(email);
		userDto.setRole(role);

		// 스프링 시큐리티 인증 객체 생성 및 설정
		CustomOAuth2User customOAuth2User = new CustomOAuth2User(userDto);
		Authentication authToken = new UsernamePasswordAuthenticationToken(customOAuth2User, null,
			customOAuth2User.getAuthorities());

		// SecurityContextHolder에 인증 정보 저장
		SecurityContextHolder.getContext().setAuthentication(authToken);

		// 다음 필터 진행
		filterChain.doFilter(request, response);
	}

	/**
	 * 요청에서 JWT 토큰을 추출하는 메서드
	 */
	private String extractToken(HttpServletRequest request) {
		String token = null;

		// 1. Authorization 헤더에서 토큰 추출
		String authorizationHeader = request.getHeader("Authorization");
		if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
			token = authorizationHeader.substring(7);
			logger.debug("JWT Token extracted from Authorization Header.");
			return token;
		}

		// 2. 쿠키에서 토큰 추출
		Cookie[] cookies = request.getCookies();
		if (cookies != null) {
			for (Cookie cookie : cookies) {
				if ("Authorization".equals(cookie.getName())) {
					token = cookie.getValue();
					logger.debug("JWT Token extracted from Cookie.");
					return token;
				}
			}
		}

		logger.debug("No JWT Token found in request.");
		return null;
	}
}
