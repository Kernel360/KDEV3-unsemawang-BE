package com.palbang.unsemawang.member.service;

import org.springframework.stereotype.Service;

import com.palbang.unsemawang.common.constants.ResponseCode;
import com.palbang.unsemawang.common.exception.GeneralException;
import com.palbang.unsemawang.member.dto.SignupExtraInfoDTO;
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
	public void signupExtraInfo(SignupExtraInfoDTO signupExtraInfoDTO) {
		//넘어온 데이터 유효성 체크(닉네임 중복 체크, 성별,생년월일, 태어난시간,양/음력, 전화번호
		//닉네임 중복체크 다시확인
		duplicateNicknameCheck(signupExtraInfoDTO.getNickname());

	}
}
