package com.palbang.unsemawang.oauth2;

import java.io.IOException;
import java.util.Collection;
import java.util.Iterator;

import org.springframework.http.ResponseCookie;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import com.palbang.unsemawang.jwt.JWTUtil;
import com.palbang.unsemawang.oauth2.dto.CustomOAuth2User;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class CustomSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {
	private final JWTUtil jwtUtil;

	public CustomSuccessHandler(JWTUtil jwtUtil) {
		this.jwtUtil = jwtUtil;
	}

	@Override
	public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
		Authentication authentication) throws IOException, ServletException {
		//OAuth2User
		CustomOAuth2User customOAuth2User = (CustomOAuth2User)authentication.getPrincipal();

		String id = customOAuth2User.getId();

		Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
		Iterator<? extends GrantedAuthority> iterator = authorities.iterator();
		GrantedAuthority auth = iterator.next();
		String role = auth.getAuthority();
		String email = customOAuth2User.getEmail();
		Boolean isJoin = customOAuth2User.getIsJoin();
		//String token = jwtUtil.createJTwt(id, email, role, 60 * 60 * 1000L); //1시간 만료
		String token = jwtUtil.createJTwt(id, email, role, 60 * 60 * 1000L * 24 * 3); //3일 만료

		//response.addCookie(createCookie("Authorization2", token));
		// ResponseCookie 추가
		ResponseCookie responseCookie = createResponseCookie("Authorization", token, 60 * 60 * 60);
		//response.setHeader("Set-Cookie", responseCookie.toString());
		response.addHeader("Set-Cookie", responseCookie.toString());
		//response.sendRedirect("https://dev.unsemawang.com");
		System.out.println("isJoin:" + isJoin);
		if (isJoin) {
			response.sendRedirect("https://www.unsemawang.com/fortune");
		} else {
			response.sendRedirect("https://www.unsemawang.com/signup");
		}
		//response.sendRedirect("http://localhost:8080/join");
	}

	private Cookie createCookie(String key, String value) {
		Cookie cookie = new Cookie(key, value);
		cookie.setMaxAge(60 * 60 * 60);
		cookie.setSecure(true);
		cookie.setPath("/");
		cookie.setDomain(".unsemawang.com");
		cookie.setHttpOnly(true);

		return cookie;
	}

	private ResponseCookie createResponseCookie(String key, String value, int maxAge) {
		// ResponseCookie 생성 시 SameSite=None 설정
		return ResponseCookie.from(key, value)
			.path("/")
			.domain("unsemawang.com") // 도메인 지정 (필요한 경우 설정)
			.sameSite("None") // SameSite=None 설정
			.secure(true)     // HTTPS에서만 전송
			.httpOnly(true)   // JavaScript 접근 불가
			.maxAge(maxAge)   // 만료 시간 설정
			.build();
	}
}
