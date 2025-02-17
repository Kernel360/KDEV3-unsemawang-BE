package com.palbang.unsemawang.member.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.palbang.unsemawang.activity.entity.ActiveMember;
import com.palbang.unsemawang.activity.repository.ActiveMemberRedisRepository;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class MemberActiveScheduler {

	private final MemberActivityService memberActivityService;
	private final ActiveMemberRedisRepository activeMemberRedisRepository;

	/**
	 * 5분마다 마지막 활동 일시 갱신
	 */
	@Scheduled(cron = "0 */5 * * * *") // 5분마다 실행
	protected void updateLastActivitySchedule() {

		List<ActiveMember> activeMembers = (ArrayList<ActiveMember>)activeMemberRedisRepository.findAll();

		activeMembers.stream()
			.filter((cache) -> !cache.isSaved()) // 저장되지 않은 정보만 업데이트
			.forEach(cache -> {
				memberActivityService.updateLastActivityTime(cache.getMemberId(), cache.getLastActiveAt());

				// 저장됨 표시
				cache.markAsSaved();
				activeMemberRedisRepository.save(cache);
			});
	}
}
