package com.palbang.unsemawang.activity.service;

import java.util.List;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import com.palbang.unsemawang.activity.constant.ActiveStatus;
import com.palbang.unsemawang.activity.dto.ActiveMemberSaveRequest;
import com.palbang.unsemawang.activity.entity.ActiveMember;
import com.palbang.unsemawang.activity.repository.ActiveMemberRedisRepository;
import com.palbang.unsemawang.common.constants.ResponseCode;
import com.palbang.unsemawang.common.exception.GeneralException;
import com.palbang.unsemawang.member.entity.Member;
import com.palbang.unsemawang.member.repository.MemberRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ActiveMemberService {
	private final ActiveMemberRedisRepository activeMemberRepository;
	private static final long ACTIVE_TTL = 900L;
	private final MemberRepository memberRepository;

	public ActiveMember saveActiveMember(ActiveMemberSaveRequest activeMemberDto) {

		ActiveMember activeMemberEntity = activeMemberDto.toActiveMemberEntity();

		return activeMemberRepository.save(activeMemberEntity);
	}

	public List<ActiveMember> findAllActiveMember() {
		return (List<ActiveMember>)activeMemberRepository.findAll();
	}

	@Cacheable(value = "activity_member", key = "#memberId")
	public ActiveMember findActiveMemberById(String memberId) {

		Member member = memberRepository.findById(memberId).orElseThrow(
			() -> new GeneralException(ResponseCode.NOT_EXIST_UNIQUE_NO)
		);

		ActiveMember activeMemberEntity = ActiveMember.builder()
			.memberId(member.getId())
			.status(ActiveStatus.INACTIVE)
			.lastActiveAt(member.getLastActivityAt())
			.build();
		return activeMemberEntity;
	}

}
