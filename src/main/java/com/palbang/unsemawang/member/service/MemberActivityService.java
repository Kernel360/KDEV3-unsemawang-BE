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
}
