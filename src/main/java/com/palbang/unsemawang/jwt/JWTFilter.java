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

	// //발급된 JWT토큰의 유효성을 검사하는 필터
	// @Override
	// protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
	// 	FilterChain filterChain) throws ServletException, IOException {
	//
	// 	String authorization = null;
	// 	logger.debug("JWTFilter doFilterInternal 호출");
	// 	Cookie[] cookies = request.getCookies();
	//
	// 	//해결방법?
	// 	if (cookies == null) {
	// 		logger.debug("cookies null");
	// 		filterChain.doFilter(request, response);
	// 		return;
	// 	}
	//
	// 	for (Cookie cookie : cookies) {
	// 		//System.out.println("쿠키이름:"+cookie.getName());
	// 		if (cookie.getName().equals("Authorization")) {
	// 			authorization = cookie.getValue();
	// 			break;
	// 		}
	// 	}
	//
	// 	//Authorization 헤더 검증
	// 	if (authorization == null) {
	// 		logger.debug("token null");
	// 		filterChain.doFilter(request, response);
	// 		return;
	// 	}
	//
	// 	//토큰 소멸 시간 검증
	// 	String token = authorization;
	// 	logger.debug("token:{}", token);
	// 	if (jwtUtil.isExpired(token)) {
	// 		logger.debug("token expired");
	// 		filterChain.doFilter(request, response);
	//
	// 		//조건이 해당되면 메소드 종료(필수)
	// 		return;
	// 	}
	// 	//토큰에서 id email role 획득
	// 	String id = jwtUtil.getId(token);
	// 	String email = jwtUtil.getEmail(token);
	// 	String role = jwtUtil.getRole(token);
	// 	logger.debug("id:{}", id);
	// 	logger.debug("email:{}", email);
	// 	logger.debug("role:{}", role);
	//
	// 	//userEntity를 생성하여 값 set
	// 	UserDto userDto = new UserDto();
	// 	userDto.setId(id);
	// 	userDto.setEmail(email);
	// 	userDto.setRole(role);
	//
	// 	//UserDetails에 회원 정보 객체 담기
	// 	CustomOAuth2User customOAuth2User = new CustomOAuth2User(userDto);
	//
	// 	//스프링 시큐리티 인증 토큰 생성
	// 	Authentication authToken = new UsernamePasswordAuthenticationToken(customOAuth2User, null,
	// 		customOAuth2User.getAuthorities());
	// 	//Authentication 객체 인증이 성공하면 SecurityContextHolder에 저장됨
	// 	SecurityContextHolder.getContext().setAuthentication(authToken);
	//
	// 	filterChain.doFilter(request, response);
	// }
}
