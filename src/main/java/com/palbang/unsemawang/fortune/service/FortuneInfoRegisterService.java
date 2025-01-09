package com.palbang.unsemawang.fortune.service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.palbang.unsemawang.common.constants.ResponseCode;
import com.palbang.unsemawang.common.exception.GeneralException;
import com.palbang.unsemawang.fortune.dto.request.FortuneInfoRequestDto;
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
public class FortuneInfoRegisterService {

	private final FortuneUserInfoRepository fortuneUserInfoRepository;
	private final MemberRepository memberRepository;
	private final UserRelationRepository userRelationRepository;

	private static final Map<Integer, String> HOUR_MAPPING;

	static {
		HOUR_MAPPING = new HashMap<>();
		HOUR_MAPPING.put(0, "모름");
		HOUR_MAPPING.put(1, "1:30 ~ 3:29");
		HOUR_MAPPING.put(2, "3:30 ~ 5:29");
		HOUR_MAPPING.put(4, "5:30 ~ 7:29");
		HOUR_MAPPING.put(6, "7:30 ~ 9:29");
		HOUR_MAPPING.put(8, "9:30 ~ 11:29");
		HOUR_MAPPING.put(10, "11:30 ~ 13:29");
		HOUR_MAPPING.put(12, "13:30 ~ 15:29");
		HOUR_MAPPING.put(14, "15:30 ~ 17:29");
		HOUR_MAPPING.put(16, "17:30 ~ 19:29");
		HOUR_MAPPING.put(18, "19:30 ~ 21:29");
		HOUR_MAPPING.put(20, "21:30 ~ 23:29");
		HOUR_MAPPING.put(22, "23:30 ~ 1:29");
	}

	/**
	 * 사주정보 등록
	 */
	public FortuneInfoRegisterResponseDto registerFortuneInfo(FortuneInfoRequestDto dto) {
		// 1. Member 조회 (회원일 경우)
		Member member = null;
		if (dto.getMemberId() != null) {
			member = memberRepository.findById(dto.getMemberId())
				.orElseThrow(() -> new GeneralException(ResponseCode.ERROR_SEARCH, "회원을 찾지 못했습니다."));
		}

		// 2. UserRelation 조회
		UserRelation relation = userRelationRepository.findByRelationName(dto.getRelationName())
			.orElseThrow(() -> new IllegalArgumentException("존재하지 않는 관계입니다."));

		// 3. 생년월일과 시간 처리
		LocalDate birthdate = LocalDate.of(dto.getYear(), dto.getMonth(),
			dto.getDay());
		String birthtime = HOUR_MAPPING.get(dto.getHour());

		// 4. FortuneUserInfo 엔티티 생성
		FortuneUserInfo fortuneUserInfo = FortuneUserInfo.builder()
			.member(member)
			.relation(relation)
			.nickname(dto.getName())
			.birthdate(birthdate)
			.birthtime(birthtime)
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
			.sex(fortuneUserInfo.getSex())
			.birthdate(birthdate)
			.birthtime(birthtime) // HH:mm 형식
			.solunar(dto.getSolunar()) // 요청값 그대로 사용
			.youn(dto.getYoun()) // 요청값 그대로 사용
			.build();
	}
}