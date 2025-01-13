package com.palbang.unsemawang.fortune.service;

import org.springframework.stereotype.Service;

import com.palbang.unsemawang.common.constants.ResponseCode;
import com.palbang.unsemawang.common.exception.GeneralException;
import com.palbang.unsemawang.fortune.dto.request.FortuneUInfoUpdateRequest;
import com.palbang.unsemawang.fortune.dto.response.UpdateResponse;
import com.palbang.unsemawang.fortune.entity.FortuneUserInfo;
import com.palbang.unsemawang.fortune.entity.UserRelation;
import com.palbang.unsemawang.fortune.repository.FortuneUserInfoRepository;
import com.palbang.unsemawang.fortune.repository.UserRelationRepository;
import com.palbang.unsemawang.member.entity.Member;
import com.palbang.unsemawang.member.repository.MemberRepository;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@Transactional
@RequiredArgsConstructor
public class FortuneUserInfoUpdateService {
	private final FortuneUserInfoRepository fortuneUserInfoRepository;
	private final MemberRepository memberRepository;
	private final UserRelationRepository userRelationRepository;

	public UpdateResponse updateFortuneUserInfo(FortuneUInfoUpdateRequest req) {
		// Member 조회
		Member member = memberRepository.findById(req.getMemberId())
			.orElseThrow(() -> new GeneralException(ResponseCode.ERROR_SEARCH, "회원을 찾지 못했습니다."));

		// 사주 정보 조회
		FortuneUserInfo fortuneUserInfo = fortuneUserInfoRepository.findById(req.getRelationId())
			.orElseThrow(() -> new GeneralException(ResponseCode.ERROR_SEARCH, "해당 사주 정보를 찾지 못했습니다."));

		// UserRelation 조회
		UserRelation userRelation = userRelationRepository.findByRelationName(req.getRelationName())
			.orElseThrow(() -> new GeneralException(ResponseCode.ERROR_SEARCH, "해당 관계명을 찾을 수 없습니다."));

		// 사주 정보 수정
		fortuneUserInfo.updateFortuneInfo(
			userRelation,
			req.getNickname(),
			req.getYear(),
			req.getMonth(),
			req.getDay(),
			req.getHour(),
			req.getSex(),
			req.getYoun(),
			req.getSolunar()
		);

		// 4. 응답 생성
		return UpdateResponse.builder()
			.relationName(fortuneUserInfo.getRelation().getRelationName())
			.nickname(fortuneUserInfo.getNickname())
			.year(fortuneUserInfo.getYear())
			.month(fortuneUserInfo.getMonth())
			.day(fortuneUserInfo.getDay())
			.hour(fortuneUserInfo.getHour())
			.sex(fortuneUserInfo.getSex())
			.youn(fortuneUserInfo.getYoun())
			.solunar(fortuneUserInfo.getSolunar())
			.build();
	}
}
