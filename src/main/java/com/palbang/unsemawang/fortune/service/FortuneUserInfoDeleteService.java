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
		try {
			// 1. Member 조회
			Member member = memberRepository.findById(memberId)
				.orElseThrow(() -> new GeneralException(ResponseCode.ERROR_SEARCH, "회원을 찾지 못했습니다."));

			// 2. 사주 정보 조회
			FortuneUserInfo fortuneUserInfo = fortuneUserInfoRepository.findById(relationId)
				.orElseThrow(() -> new GeneralException(ResponseCode.ERROR_SEARCH, "해당 사주 정보를 찾지 못했습니다."));

			// 3. 삭제 처리
			fortuneUserInfo.deleted();

			// 성공 응답 반환
			return DeleteResponseDto.builder()
				.responseCode(ResponseCode.SUCCESS_DELETE)
				.message("사주 정보 삭제 성공")
				.build();

		} catch (GeneralException e) {
			// GeneralException의 경우 실패 코드 반환
			return DeleteResponseDto.builder()
				.responseCode(e.getErrorCode())
				.message(e.getMessage())
				.build();
		} catch (Exception e) {
			// 알 수 없는 예외 처리
			return DeleteResponseDto.builder()
				.responseCode(ResponseCode.DEFAULT_INTERNAL_SERVER_ERROR)
				.message("알 수 없는 서버 오류가 발생했습니다.")
				.build();
		}
	}
}
