package com.palbang.unsemawang.activity.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.palbang.unsemawang.activity.entity.ActiveMember;
import com.palbang.unsemawang.activity.repository.ActiveMemberRedisRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ActiveMemberRedisService {
	private final ActiveMemberRedisRepository activeMemberRepository;
	private static final long ACTIVE_TTL = 900L;

	public ActiveMember saveAndUpdateActiveMember(String memberId) {

		ActiveMember activeMember = ActiveMember.builder()
			.memberId(memberId)
			.ttl(ACTIVE_TTL)
			.build();

		return activeMemberRepository.save(activeMember);
	}

	public List<ActiveMember> findAllActiveMember() {
		return (List<ActiveMember>)activeMemberRepository.findAll();
	}
}
