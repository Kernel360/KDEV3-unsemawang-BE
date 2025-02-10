package com.palbang.unsemawang.activity.aop;

import org.aspectj.lang.annotation.Aspect;
import org.springframework.data.redis.core.RedisTemplate;
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

	private final RedisTemplate redisTemplate;
	private final ActiveMemberService activeMemberService;

	public boolean isRedisConnected() {
		try {
			String pingResult = redisTemplate.getConnectionFactory().getConnection().ping();
			return "PONG".equalsIgnoreCase(pingResult);
		} catch (Exception e) {
			return false;
		}
	}

	/**
	 * 회원이 API 요청을 수행한 후 활동 내역을 Redis에 저장
	 */
	// @AfterReturning("execution(* com.palbang.unsemawang..controller..*(..)) && !@annotation(com.palbang.unsemawang.activity.aop.NoTracking)")
	public void trackUserActivity() {

		if (!isRedisConnected()) {
			log.error("레디스 연결 정보 없음");
			return;
		}

		Authentication auth = SecurityContextHolder.getContext().getAuthentication();

		if (auth == null || !auth.isAuthenticated()) {
			log.warn("인증 정보가 없습니다");
			return;
		}

		String memberId = ((CustomOAuth2User)auth.getPrincipal()).getId();

		// 레디스 활동 리스트 갱신
		ActiveMember savedActiveMember = activeMemberService.saveAndUpdateActiveMember(memberId);
		log.info("저장된 회원 활동 정보: {}", savedActiveMember);

	}
}
