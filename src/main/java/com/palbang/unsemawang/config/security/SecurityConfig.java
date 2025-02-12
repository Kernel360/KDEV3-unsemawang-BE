package com.palbang.unsemawang.config.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.palbang.unsemawang.jwt.JWTFilter;
import com.palbang.unsemawang.jwt.JWTUtil;
import com.palbang.unsemawang.oauth2.CustomSuccessHandler;
import com.palbang.unsemawang.oauth2.service.CustomOAuth2UserService;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

	private final CustomOAuth2UserService customOAuth2UserService;
	private final CustomSuccessHandler customSuccessHandler;
	private final JWTUtil jwtUtil;

	public SecurityConfig(final CustomOAuth2UserService customOAuth2UserService,
		CustomSuccessHandler customSuccessHandler, JWTUtil jwtUtil) {
		this.customOAuth2UserService = customOAuth2UserService;
		this.customSuccessHandler = customSuccessHandler;
		this.jwtUtil = jwtUtil;
	}

	@Bean
	public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
		http
			.csrf(csrf -> csrf.disable())  // CSRF 비활성화
			.formLogin(formLogin -> formLogin.disable()) // 기본 로그인 방식 비활성화
			.httpBasic(httpBasic -> httpBasic.disable()) // HTTP Basic 인증 비활성화
			.sessionManagement(
				session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)) // JWT 사용을 위해 세션 관리 비활성화
			.addFilterBefore(new JWTFilter(jwtUtil),
				UsernamePasswordAuthenticationFilter.class) // ✅ JWT 필터를 UsernamePasswordAuthenticationFilter 앞에 추가
			.oauth2Login(oauth2 -> oauth2
				.userInfoEndpoint(userInfo -> userInfo.userService(customOAuth2UserService))
				.successHandler(customSuccessHandler)
			);

		// ✅ 인증이 필요한 엔드포인트 지정
		http.authorizeHttpRequests(auth -> auth
			.requestMatchers("/favicon.ico").permitAll()
			//.requestMatchers("/chat/rooms/**").authenticated()
			.anyRequest().permitAll());

		return http.build();
	}

	/**
	 * 패스워드 암호화를 위한 BCryptPasswordEncoder Bean 등록
	 * @return PasswordEncoder 객체
	 */
	@Bean
	public BCryptPasswordEncoder bCryptPasswordEncoder() {
		return new BCryptPasswordEncoder();
	}
}
