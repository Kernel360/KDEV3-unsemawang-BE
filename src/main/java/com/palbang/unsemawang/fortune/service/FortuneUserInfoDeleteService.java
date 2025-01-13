package com.palbang.unsemawang.fortune.service;

import org.springframework.stereotype.Service;

import com.palbang.unsemawang.common.constants.ResponseCode;
import com.palbang.unsemawang.common.exception.GeneralException;
import com.palbang.unsemawang.fortune.dto.response.DeleteResponseDto;
import com.palbang.unsemawang.fortune.entity.FortuneUserInfo;
import com.palbang.unsemawang.fortune.repository.FortuneUserInfoRepository;
import com.palbang.unsemawang.member.entity.Member;
import com.palbang.unsemawang.member.repository.MemberRepository;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@Transactional
@RequiredArgsConstructor
public class FortuneUserInfoDeleteService {
	private final FortuneUserInfoRepository fortuneUserInfoRepository;
	private final MemberRepository memberRepository;

	public DeleteResponseDto deleteFortuneUserInfo(String memberId, Long relationId) {

		// 1. Member 조회
		Member member = memberRepository.findById(memberId)
			.orElseThrow(() -> new GeneralException(ResponseCode.ERROR_SEARCH, "회원을 찾지 못했습니다."));

		// 2. 사주 정보 조회
		FortuneUserInfo fortuneUserInfo = fortuneUserInfoRepository.findById(relationId)
			.orElseThrow(() -> new GeneralException(ResponseCode.ERROR_SEARCH, "해당 사주 정보를 찾지 못했습니다."));

		// 조회된 fortuneUserInfo의 idDeleted가 false면 true로 바꾸기
		if (!fortuneUserInfo.getIsDeleted()) {
			fortuneUserInfo.deleted();
		} else {
			return DeleteResponseDto.builder()
				.message("이미 삭제된 사주 정보입니다.")
				.build();
		}

		// 성공 응답 반환
		return DeleteResponseDto.builder()
			.message("사주 정보 삭제 성공")
			.build();
	}
}
