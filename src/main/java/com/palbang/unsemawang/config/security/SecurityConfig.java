package com.palbang.unsemawang.config.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.client.web.OAuth2LoginAuthenticationFilter;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

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
	public CorsConfigurationSource corsConfigurationSource() {
		CorsConfiguration configuration = new CorsConfiguration();
		configuration.addAllowedOrigin("https://www.unsemawang.com"); // 허용할 도메인
		configuration.addAllowedOrigin("https://dev.unsemawang.com"); // 허용할 도메인
		configuration.addAllowedOrigin("http://localhost:8080");
		configuration.setAllowCredentials(true); // 쿠키 전송 허용
		configuration.addAllowedMethod("*"); // 모든 HTTP 메서드 허용
		configuration.addAllowedHeader("*"); // 모든 헤더 허용
		configuration.addExposedHeader("Set-Cookie"); // 클라이언트에서 쿠키 확인 가능
		configuration.addExposedHeader("Authorization"); // 클라이언트에서 쿠키 확인 가능

		UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
		source.registerCorsConfiguration("/**", configuration); // 모든 경로에 CORS 설정 적용
		return source;
	}

	@Bean
	public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
		//csrf disable

		http
			//.cors(cors -> cors.configurationSource(corsConfigurationSource()))
			.csrf((auth) -> auth.disable());

		//기본 Form 로그인 방식 disable
		http
			.formLogin((auth) -> auth.disable());

		//HTTP Basic 인증 방식 disable
		http
			.httpBasic((auth) -> auth.disable());

		//JWTFilter 추가
		http
			.addFilterBefore(corsFilter(), OAuth2LoginAuthenticationFilter.class) // CORS 필터 추가
			.addFilterAfter(new JWTFilter(jwtUtil), OAuth2LoginAuthenticationFilter.class);

		//oauth2
		http
			.oauth2Login(oauth2 -> oauth2
				.userInfoEndpoint((userInfoEndpointConfig -> userInfoEndpointConfig
					.userService(customOAuth2UserService)))
				.successHandler(customSuccessHandler)
			);

		//경로별 인가 작업
		//        http
		//                .authorizeHttpRequests((auth) -> auth
		//                        .requestMatchers("/").permitAll()
		//                        .anyRequest().authenticated());

		//세션 설정 : STATELESS
		//        http
		//                .sessionManagement((session) -> session
		//                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS));

		http.authorizeHttpRequests(auth -> auth
			.requestMatchers("/favicon.ico").permitAll()
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

	@Bean
	public CorsFilter corsFilter() {
		return new CorsFilter(corsConfigurationSource()); // CorsConfigurationSource를 기반으로 CorsFilter 생성
	}

}
