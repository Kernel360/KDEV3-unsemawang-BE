package com.palbang.unsemawang.fortune.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.palbang.unsemawang.common.constants.ResponseCode;
import com.palbang.unsemawang.common.exception.GeneralException;
import com.palbang.unsemawang.fortune.dto.response.FortuneUserInfoReadResponseDto;
import com.palbang.unsemawang.fortune.entity.FortuneUserInfo;
import com.palbang.unsemawang.fortune.repository.FortuneUserInfoRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class FortuneUserInfoReadService {
	private final FortuneUserInfoRepository fortuneUserInfoRepository;

	/**
	 * 특정 사용자 ID로 사주 정보 조회
	 */
	public List<FortuneUserInfoReadResponseDto> fortuneInfoListRead(String memberId) {

		List<FortuneUserInfo> userInfoList = fortuneUserInfoRepository.findByMemberId(memberId);

		if (userInfoList.isEmpty()) {
			throw new GeneralException(ResponseCode.ERROR_SEARCH, "해당 회원의 사주 정보를 찾을 수 없습니다.");
		}

		// 조회 결과를 FortuneUserInfoReadResponseDto로 변환 후 반환
		return userInfoList.stream()
			.map(this::toResponseDto)
			.collect(Collectors.toList());
	}

	/**
	 * FortuneUserInfo를 FortuneUserInfoReadResponseDto로 변환
	 */
	private FortuneUserInfoReadResponseDto toResponseDto(FortuneUserInfo fortuneUserInfo) {
		return FortuneUserInfoReadResponseDto.builder()
			.relationId(fortuneUserInfo.getId())
			.relationName(fortuneUserInfo.getRelation().getRelationName())
			.name(fortuneUserInfo.getNickname())
			.sex(fortuneUserInfo.getSex())
			.year(fortuneUserInfo.getYear())
			.month(fortuneUserInfo.getMonth())
			.day(fortuneUserInfo.getDay())
			.hour(fortuneUserInfo.getHour())
			.solunar(fortuneUserInfo.getSolunar())
			.youn(fortuneUserInfo.getYoun())
			.build();
	}
}
