package com.palbang.unsemawang.fortune.service;

import org.springframework.stereotype.Service;

import com.palbang.unsemawang.common.constants.ResponseCode;
import com.palbang.unsemawang.common.exception.GeneralException;
import com.palbang.unsemawang.fortune.dto.response.FortuneUserInfoDeleteResponseDto;
import com.palbang.unsemawang.fortune.entity.FortuneUserInfo;
import com.palbang.unsemawang.fortune.repository.FortuneUserInfoRepository;
import com.palbang.unsemawang.member.repository.MemberRepository;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@Transactional
@RequiredArgsConstructor
public class FortuneUserInfoDeleteService {
	private final FortuneUserInfoRepository fortuneUserInfoRepository;
	private final MemberRepository memberRepository;

	public FortuneUserInfoDeleteResponseDto deleteFortuneUserInfo(String memberId, Long fortuneUserInfoId) {

		// 1. Member 조회
		memberRepository.findById(memberId)
			.orElseThrow(() -> new GeneralException(ResponseCode.ERROR_SEARCH, "회원을 찾지 못했습니다."));

		// 2. 사주 정보 조회
		FortuneUserInfo fortuneUserInfo = fortuneUserInfoRepository.findById(fortuneUserInfoId)
			.orElseThrow(() -> new GeneralException(ResponseCode.ERROR_SEARCH, "해당 사주 정보를 찾지 못했습니다."));

		// 본인 사주 정보이면 삭제 불가
		if (fortuneUserInfo.getRelation().getId() == 1) {
			throw new GeneralException(ResponseCode.NOT_DELETE_SELF_RELATION);
		}

		// 조회된 fortuneUserInfo의 idDeleted가 false면 true로 바꾸기
		if (!fortuneUserInfo.getIsDeleted()) {
			fortuneUserInfo.deleted();
		} else {
			return FortuneUserInfoDeleteResponseDto.builder()
				.message("이미 삭제된 사주 정보입니다.")
				.build();
		}

		// 성공 응답 반환
		return FortuneUserInfoDeleteResponseDto.builder()
			.message("사주 정보 삭제 성공")
			.build();
	}
}
