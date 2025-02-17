package com.palbang.unsemawang.member.service;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.palbang.unsemawang.common.constants.ResponseCode;
import com.palbang.unsemawang.common.exception.GeneralException;
import com.palbang.unsemawang.member.entity.Member;
import com.palbang.unsemawang.member.repository.MemberRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class MemberActivityService {
	private final MemberRepository memberRepository;

	// 회원 마지막 활동 일시 갱신
	@Transactional
	public Member updateLastActivityNow(String memberId) {
		Member member = memberRepository.findById(memberId)
			.orElseThrow(() -> new GeneralException(ResponseCode.NOT_EXIST_UNIQUE_NO, "회원을 찾을 수 없습니다"));

		member.updateLastActivityAt();

		return member;
	}

	@Transactional(readOnly = true)
	public List<Member> findRecentActiveMembersAfter(LocalDateTime asOfDateTime) {
		return memberRepository.findAllByLastActivityAtAfter(asOfDateTime);
	}

	/**
	 * member JPA 엔티티에 마지막 활동 시간 갱신
	 * @param memberId
	 * @param LastActivityTime
	 */
	@Transactional(rollbackFor = Exception.class)
	public void updateLastActivityTime(String memberId, LocalDateTime LastActivityTime) {
		Member member = memberRepository.findById(memberId)
			.orElse(null);

		if (member == null) {
			log.warn("유효하지 않은 회원 ID의 마지막 활동 시간 갱신 시도가 이루어졌습니다");
			return;
		}

		member.updateLastActivityAt(LastActivityTime);

	}
}
