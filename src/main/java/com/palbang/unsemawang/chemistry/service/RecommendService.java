package com.palbang.unsemawang.chemistry.service;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.palbang.unsemawang.chemistry.dto.response.ChemistryRecommendResponse;
import com.palbang.unsemawang.chemistry.entity.MemberMatchingScore;
import com.palbang.unsemawang.chemistry.repository.MemberMatchingScoreRepository;
import com.palbang.unsemawang.common.constants.ResponseCode;
import com.palbang.unsemawang.common.exception.GeneralException;
import com.palbang.unsemawang.common.util.file.service.FileService;
import com.palbang.unsemawang.fortune.entity.FortuneUserInfo;
import com.palbang.unsemawang.fortune.repository.FortuneUserInfoRepository;
import com.palbang.unsemawang.member.entity.Member;
import com.palbang.unsemawang.member.repository.MemberRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class RecommendService {
	private final MemberMatchingScoreRepository memberMatchingScoreRepository;
	private final MemberRepository memberRepository;
	private final FortuneUserInfoRepository fortuneUserInfoRepository;
	private final FileService fileService;
	private final StringRedisTemplate redisTemplate;

	private static final String REDIS_KEY_PREFIX = "matching_status:";

	private static final long MATCHING_NUMBER = 4;

	@Transactional(readOnly = true)
	public List<ChemistryRecommendResponse> getTopShuffleMatches(String memberId) {

		// Redis에서 상태 확인
		String status = redisTemplate.opsForValue().get(REDIS_KEY_PREFIX + memberId);

		if ("processing".equals(status)) {
			throw new GeneralException(ResponseCode.MATCHING_PROCESSING);
		}

		Member member = memberRepository.findById(memberId)
			.orElseThrow(() -> new GeneralException(ResponseCode.NOT_EXIST_MEMBER_ID));

		// 레디스 도입 전: 기본적으로 60점 이상인 사용자만 선택
		int minScore = 60;

		// 레디스 도입 후
		// Redis에서 새로고침 횟수 가져오기
		// int refreshCount = refreshCountService.getRefreshCount(memberId);

		// 새로고침 횟수에 따라 점수 범위 확장 가능하도록
		/*
		int minScore = switch (refreshCount) {
			case 0 -> 90; // 첫 추천: 90점 이상
			case 1 -> 80; // 1회 새로고침: 80점 이상
			case 2 -> 70; // 2회 새로고침: 70점 이상
			default -> 50; // 3회 이상: 50점 이상 허용
		};
		*/

		List<MemberMatchingScore> matchingScores = memberMatchingScoreRepository
			.findByMemberIdAndScalingScoreGreaterThanEqualOrderByScalingScoreDesc(member.getId(), minScore);

		if (matchingScores.isEmpty()) {
			throw new GeneralException(ResponseCode.NOT_MATCHING_PEOPLE);
		}

		// 랜덤 섞기 (shuffle 적용)
		Collections.shuffle(matchingScores);

		// MATCHING_NUMBER 가져오기
		List<MemberMatchingScore> topMatches = matchingScores.stream()
			.limit(MATCHING_NUMBER)
			.toList();

		// 새로고침 횟수 증가, 레디스 도입 후 사용
		// refreshCountService.incrementRefreshCount(memberId);

		// 응답 객체 변환
		return topMatches.stream()
			.map(score -> {
				String matchMemberId = score.getMatchMember().getId();
				String imgUrl = fileService.getProfileImgUrl(matchMemberId);
				FortuneUserInfo fortuneUserInfo = fortuneUserInfoRepository.findByMemberIdRelationIdIsOne(matchMemberId)
					.orElseThrow(() -> new GeneralException(ResponseCode.ERROR_SEARCH));

				return ChemistryRecommendResponse.from(score, fortuneUserInfo, imgUrl);
			})
			.collect(Collectors.toList());
	}
}