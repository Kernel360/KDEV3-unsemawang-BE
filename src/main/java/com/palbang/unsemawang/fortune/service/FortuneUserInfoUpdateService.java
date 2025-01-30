package com.palbang.unsemawang.fortune.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.palbang.unsemawang.common.constants.ResponseCode;
import com.palbang.unsemawang.common.exception.GeneralException;
import com.palbang.unsemawang.fortune.dto.request.FortuneInfoUpdateRequest;
import com.palbang.unsemawang.fortune.dto.response.FortuneUserInfoUpdateResponse;
import com.palbang.unsemawang.fortune.entity.FortuneUserInfo;
import com.palbang.unsemawang.fortune.entity.UserRelation;
import com.palbang.unsemawang.fortune.repository.FortuneUserInfoRepository;
import com.palbang.unsemawang.fortune.repository.UserRelationRepository;
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

	public FortuneUserInfoUpdateResponse updateFortuneUserInfo(FortuneInfoUpdateRequest req) {
		// Member 조회
		memberRepository.findById(req.getMemberId())
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
		fortuneUserInfoRepository.save(fortuneUserInfo);

		// 4. 응답 생성
		return FortuneUserInfoUpdateResponse.builder()
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
	public void updateFortuneUserNickname(String id, String nickname) {
		List<FortuneUserInfo> fortuneUserInfoList = fortuneUserInfoRepository.findByMemberIdAndRelation(id,"본인");

		if (fortuneUserInfoList.isEmpty()) {
			throw new GeneralException(ResponseCode.ERROR_SEARCH, "해당 회원의 본인 사주정보를 찾지 못했습니다.");
		}

		// 첫 번째 사주 정보를 가져와서 닉네임 변경
		FortuneUserInfo fortuneUserInfo = fortuneUserInfoList.get(0);
		fortuneUserInfo.updateFortuneNickname(nickname);

		// 변경된 정보 저장
		fortuneUserInfoRepository.save(fortuneUserInfo);
	}
}
