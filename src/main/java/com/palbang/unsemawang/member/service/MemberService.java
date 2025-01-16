package com.palbang.unsemawang.member.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.palbang.unsemawang.common.constants.ResponseCode;
import com.palbang.unsemawang.common.exception.GeneralException;
import com.palbang.unsemawang.member.dto.MemberProfileDto;
import com.palbang.unsemawang.member.dto.SignupExtraInfoRequest;
import com.palbang.unsemawang.member.entity.Member;
import com.palbang.unsemawang.member.repository.MemberRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class MemberService {
	private final MemberRepository memberRepository;

	// nickname 중복 여부 확인
	public void duplicateNicknameCheck(String nickname) {
		if (memberRepository.findByNickname(nickname).isPresent()) {
			throw new GeneralException(ResponseCode.DUPLICATED_VALUE);
		}
	}

	//회원 추가 정보 입력
	@Transactional(rollbackFor = Exception.class)
	public void signupExtraInfo(SignupExtraInfoRequest signupExtraInfo) {
		// 회원 정보 조회
		Member member = memberRepository.findById(signupExtraInfo.getId())
			.orElseThrow(() -> new GeneralException(ResponseCode.NOT_EXIST_UNIQUE_NO, "회원을 찾을 수 없습니다"));

		// 닉네임 중복체크
		duplicateNicknameCheck(signupExtraInfo.getNickname());

		// 추가 정보 업데이트
		member.updateExtraInfo(signupExtraInfo);

		//회원가입 추가정보 입력 여부 값 변경
		member = member.toBuilder()
			.isJoin(true)
			.build();

		memberRepository.save(member);

	}

	public MemberProfileDto getMemberProfile(String id) {

		Member member = memberRepository.findById(id)
			.orElseThrow(() -> new GeneralException(ResponseCode.NOT_EXIST_UNIQUE_NO, "회원을 찾을 수 없습니다"));

		MemberProfileDto memberProfileDto = MemberProfileDto.builder()
			.role(member.getRole())
			.email(member.getEmail())
			.nickname(member.getNickname())
			.isJoin(member.getIsJoin())
			.profileUrl(member.getProfileUrl())
			.point(member.getPoint())
			.build();

		return memberProfileDto;
	}
}
