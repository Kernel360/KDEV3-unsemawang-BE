package com.palbang.unsemawang;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.test.context.support.WithSecurityContextFactory;

import com.palbang.unsemawang.oauth2.dto.CustomOAuth2User;
import com.palbang.unsemawang.oauth2.dto.UserDto;

public class WithCustomMockSecurityContextFactory implements WithSecurityContextFactory<WithCustomMockUser> {

	@Override
	public SecurityContext createSecurityContext(WithCustomMockUser annotation) {
		// 어노테이션 속성 값들을 가지고 UserDto 객체 생성
		UserDto userDto = new UserDto(
			annotation.memberId(),
			annotation.email(),
			annotation.role()
		);

		// UserDetails 구현체에 회원 정보 객체 담기
		CustomOAuth2User customOAuth2User = new CustomOAuth2User(userDto);

		//스프링 시큐리티 인증 토큰 생성
		Authentication authToken = new UsernamePasswordAuthenticationToken(customOAuth2User, null,
			customOAuth2User.getAuthorities());

		// 생성된 인증 토큰을 SecurityContext에 저장
		SecurityContext context = SecurityContextHolder.getContext();
		context.setAuthentication(authToken);

		// 시큐리티 컨텍스트 반환
		return context;
	}
}
