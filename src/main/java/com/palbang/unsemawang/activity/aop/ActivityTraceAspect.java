package com.palbang.unsemawang.activity.aop;

import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import com.palbang.unsemawang.activity.entity.ActiveMember;
import com.palbang.unsemawang.activity.service.ActiveMemberService;
import com.palbang.unsemawang.oauth2.dto.CustomOAuth2User;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Aspect
@Component
@RequiredArgsConstructor
public class ActivityTraceAspect {

	private final ActiveMemberService activeMemberService;

	/**
	 * 회원이 API 요청을 수행한 후 활동 내역을 Redis에 저장
	 */
	@AfterReturning("execution(* com.palbang.unsemawang..controller..*(..)) && !@annotation(com.palbang.unsemawang.activity.aop.NoTracking)")
	public void trackUserActivity() {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();

		if (auth == null || !auth.isAuthenticated()) {
			log.warn("인증 정보가 없습니다");
			return;
		}

		Object principal = auth.getPrincipal(); // Principal 확인

		String memberId;
		if (principal instanceof CustomOAuth2User) {
			memberId = ((CustomOAuth2User)principal).getId();
		} else if (principal instanceof String) {
			memberId = (String)principal; // Principal이 String인 경우
		} else {
			log.error("예상치 못한 Principal 타입: {}", principal.getClass().getName());
			return;
		}

		log.info("회원 활동 기록: {}", memberId);
		activeMemberService.saveAndUpdateActiveMember(memberId);
	}
}
