package com.palbang.unsemawang.fortune.service;

import java.time.LocalDateTime;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.palbang.unsemawang.common.constants.ResponseCode;
import com.palbang.unsemawang.common.exception.GeneralException;
import com.palbang.unsemawang.fortune.dto.request.FortuneInfoRegisterRequest;
import com.palbang.unsemawang.fortune.dto.response.FortuneInfoRegisterResponseDto;
import com.palbang.unsemawang.fortune.entity.FortuneUserInfo;
import com.palbang.unsemawang.fortune.entity.UserRelation;
import com.palbang.unsemawang.fortune.repository.FortuneUserInfoRepository;
import com.palbang.unsemawang.fortune.repository.UserRelationRepository;
import com.palbang.unsemawang.member.entity.Member;
import com.palbang.unsemawang.member.repository.MemberRepository;

import lombok.RequiredArgsConstructor;

@Service
@Transactional
@RequiredArgsConstructor
public class FortuneUserInfoRegisterService {

	private final FortuneUserInfoRepository fortuneUserInfoRepository;
	private final MemberRepository memberRepository;
	private final UserRelationRepository userRelationRepository;

	/**
	 * 사주정보 등록
	 */
	public FortuneInfoRegisterResponseDto registerFortuneInfo(FortuneInfoRegisterRequest dto) {
		// 1. Member 조회
		Member member = null;
		if (dto.getMemberId() != null) {
			member = memberRepository.findById(dto.getMemberId())
				.orElseThrow(() -> new GeneralException(ResponseCode.ERROR_SEARCH, "회원을 찾지 못했습니다."));
		}

		// 2. UserRelation 조회
		UserRelation relation = userRelationRepository.findByRelationName(dto.getRelationName())
			.orElseThrow(() -> new IllegalArgumentException("존재하지 않는 관계입니다."));

		// 3. FortuneUserInfo 엔티티 생성
		FortuneUserInfo fortuneUserInfo = FortuneUserInfo.builder()
			.member(member)
			.relation(relation)
			.nickname(dto.getName())
			.year(dto.getYear())
			.month(dto.getMonth())
			.day(dto.getDay())
			.hour(dto.getHour())
			.sex(dto.getSex())
			.youn(dto.getYoun())
			.solunar(dto.getSolunar())
			.registeredAt(LocalDateTime.now())
			.updatedAt(LocalDateTime.now())
			.build();

		// 5. 데이터 저장
		fortuneUserInfoRepository.save(fortuneUserInfo);

		// 6. 응답 DTO 생성 및 반환
		return FortuneInfoRegisterResponseDto.builder()
			.memberId(member != null ? member.getId() : null)
			.relationName(relation.getRelationName())
			.name(fortuneUserInfo.getNickname())
			.year(fortuneUserInfo.getYear())
			.month(fortuneUserInfo.getMonth())
			.day(fortuneUserInfo.getDay())
			.hour(dto.getHour())
			.sex(fortuneUserInfo.getSex())
			.solunar(dto.getSolunar())
			.youn(dto.getYoun())
			.build();
	}
}